package com.watchedit.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TopShowFragment extends Fragment implements AsyncResponse{
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
    public TopShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/top_rated?api_key="+APIKEYthemovieDB+"&language=en-US");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_top_show, container, false);

        return view;
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

        list=(ListView)getActivity().findViewById(R.id.list1);
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
