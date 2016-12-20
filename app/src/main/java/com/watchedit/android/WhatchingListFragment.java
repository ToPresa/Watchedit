package com.watchedit.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WhatchingListFragment extends Fragment implements AsyncResponse{
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUser = mRootRef.child("user");
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
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";
    public WhatchingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_whatching_list, container, false);
        if (AccessToken.getCurrentAccessToken() != null) {
            TextView tt=(TextView) v.findViewById(R.id.tesss);
            ImageView ee=(ImageView) v.findViewById(R.id.qwee);
            ee.setVisibility(View.INVISIBLE);
            tt.setVisibility(View.INVISIBLE);

            mUser.child(AccessToken.getCurrentAccessToken().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //for(int i=0;i<dataSnapshot.getValue().)
                    if(dataSnapshot.getValue()!=null) {
                        String a = dataSnapshot.getValue().toString().replace("{", "");
                        String b = a.replace("}", "");
                        String c[] = b.split(", ");
                        for (int i = 0; i < c.length; i++) {
                            String d[] = c[i].split("=");
                            //pedido
                            request(d[1]);

                        }
                    }
                    else{
                        //mensagem vazia
                        TextView tt=(TextView) v.findViewById(R.id.tesss);
                        tt.setText(R.string.empty);
                        ImageView ee=(ImageView) v.findViewById(R.id.qwee);
                        ee.setVisibility(View.VISIBLE);
                        tt.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("tt", "onCancelled", databaseError.toException());
                }
            });
        }
        else{
            TextView tt=(TextView) v.findViewById(R.id.tesss);
            tt.setText(R.string.login);
            ImageView ee=(ImageView) v.findViewById(R.id.qwee);
            ee.setVisibility(View.VISIBLE);
            tt.setVisibility(View.VISIBLE);        }
        return v;
    }
    public void request(String req){

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/"+req+"?api_key="+APIKEYthemovieDB+"&language=en-US");
    }
    @Override
    public void processFinish(String asyncresult){

        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.

        try {
            JSONObject json = new JSONObject(asyncresult);
                itemname.add((String) (json.getString("name")));
                itemimg.add("https://image.tmdb.org/t/p/w500"+(json.getString("poster_path")));
                itemrate.add((String) (json.getString("vote_average")));
                itemdate.add("First aired: "+ (json.getString("first_air_date")));
                itemid.add((String) (json.getString("id")));

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

        list=(ListView)getActivity().findViewById(R.id.list2345);
        DisplayWatching adapter=new DisplayWatching(this.getActivity(), names, imgs, rates, dates,ids);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub


                String Slecteditem= ids[+position];
                //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(getActivity(), TvShowInf.class);
                myIntent.putExtra("nameTvShow", Slecteditem);

                getActivity().startActivity(myIntent);
            }
        });
        /**/
    }

}
