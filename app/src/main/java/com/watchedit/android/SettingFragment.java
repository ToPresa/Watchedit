package com.watchedit.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends Fragment implements AsyncResponse {

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

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/top_rated?api_key="+APIKEYthemovieDB+"&language=en-US");
        //call.execute("https://api.themoviedb.org/3/search/tv?api_key="+APIKEYthemovieDB+"&language=en-US&query="+"string"+"&page=1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EditText addCourseText = (EditText) getActivity().findViewById(R.id.editText12);
        addCourseText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            EditText addCourseText = (EditText) getActivity().findViewById(R.id.editText12);

                            //request(addCourseText.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    public void request(String req){

        APIcall call = new APIcall();
        call.delegate=this;
       call.execute("https://api.themoviedb.org/3/search/tv?api_key="+APIKEYthemovieDB+"&language=en-US&query="+req+"&page=1");
    }

    @Override
    public void processFinish(String asyncresult){
        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.

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

        list=(ListView)getActivity().findViewById(R.id.list234);
        Display adapter=new Display(this.getActivity(), names, imgs, rates, dates);

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
    }

}
