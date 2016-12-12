package com.watchedit.android;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Banners extends AppCompatActivity implements AsyncResponse{
String id;
    ListView list;
    List<String> itemname = new ArrayList<String>();
    List<String> itemimg = new ArrayList<String>();
    List<String> itemrate = new ArrayList<String>();
    List<String> itemdate = new ArrayList<String>();
    List<String> itemid = new ArrayList<String>();
    String[] names;
    String[] imgs;
    String[] rates;
    String[] dates;
    String[] ids;
    AccessTokenTracker accessTokenTracker;
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_banners);

        Intent intent = getIntent();
        id= intent.getExtras().getString("id");

        TextView txtBanners = (TextView) findViewById(R.id.textView);
        txtBanners.setTypeface(null, Typeface.BOLD);

        addListenerOnButton();

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/"+id+"/similar?api_key="+APIKEYthemovieDB+"&language=en-US");
    }
    @Override
    public void processFinish(String asyncresult) {
        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.
        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.e("dss",asyncresult);

        if(AccessToken.getCurrentAccessToken()==null) {
            Log.e("qq",asyncresult);
        }
        else{
            try {
                JSONObject json = new JSONObject(asyncresult);
                JSONArray a = json.getJSONArray("results");
                for (int i = 0; i < a.length() && i<10; ++i) {
                    json = a.getJSONObject(i);
                    itemname.add((String) (json.getString("name")));
                    itemimg.add("https://image.tmdb.org/t/p/w500"+(json.getString("poster_path")));
                    itemrate.add((String) (json.getString("vote_average")));
                    itemdate.add("First aired: "+ (json.getString("first_air_date")));
                    itemid.add((String) (json.getString("id")));
                }
            }catch(Exception e){
                return;
            }

            names = new String[ itemname.size() ];
            itemname.toArray( names );
            imgs = new String[ itemimg.size() ];
            itemimg.toArray( imgs );
            rates = new String[ itemrate.size() ];
            itemrate.toArray( rates );
            dates = new String[ itemdate.size() ];
            itemdate.toArray( dates );
            ids = new String[ itemid.size() ];
            itemid.toArray( ids );

            list=(ListView)findViewById(R.id.list222);
            Display adapter=new Display(this, names, imgs, rates, dates);

            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem= ids[+position];
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(view.getContext(), TvShowInf.class);
                    myIntent.putExtra("nameTvShow", Slecteditem);

                    startActivity(myIntent);
                    finish();
                }
            });
        }

    }
    public void addListenerOnButton() {

        ImageView img = (ImageView) findViewById(R.id.imageView1);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                onBackPressed();
            }
        });
    }
}
