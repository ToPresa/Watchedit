package com.watchedit.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotShowFragment extends Fragment implements AsyncResponse {
    Button btn;
    int filter_rating=999;
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
    public HotShowFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        APIcall call = new APIcall();
        call.delegate=this;
       call.execute("https://api.themoviedb.org/3/tv/popular?api_key="+APIKEYthemovieDB+"&language=en-US");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv=inflater.inflate(R.layout.fragment_hot_show, container, false);

        btn = (Button) vv.findViewById(R.id.button99);
        final SeekBar seek_bar = (SeekBar) vv.findViewById(R.id.seekBar);
        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        btn.setText("Filter: " +progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v){
                String buttontxt = btn.getText().toString();
                String[] parts = buttontxt.split(":");
                filter_rating = Integer.parseInt(parts[1].replaceAll("\\s+",""));
                request();
            }


        });

        return vv;
    }

    public void request(){

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/popular?api_key="+APIKEYthemovieDB+"&language=en-US");
    }


    @Override
    public void processFinish(String asyncresult){
        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.

    try {
        JSONObject json = new JSONObject(asyncresult);
        JSONArray a = json.getJSONArray("results");
        for (int i = 0; i < a.length(); ++i) {
            json = a.getJSONObject(i);
            if(filter_rating != 999) {
                String rate = json.getString("vote_average");
                if((double)filter_rating <= Double.parseDouble(rate)){
                    itemname.add((String) (json.getString("name")));
                    itemimg.add("https://image.tmdb.org/t/p/w500"+(json.getString("poster_path")));
                    itemrate.add((String) (json.getString("vote_average")));
                    itemdate.add("First aired: "+ (json.getString("first_air_date")));
                    itemid.add((String) (json.getString("id")));
                }
            } else {
                itemname.add((String) (json.getString("name")));
                itemimg.add("https://image.tmdb.org/t/p/w500"+(json.getString("poster_path")));
                itemrate.add((String) (json.getString("vote_average")));
                itemdate.add("First aired: "+ (json.getString("first_air_date")));
                itemid.add((String) (json.getString("id")));
            }
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

        list=(ListView)getActivity().findViewById(R.id.list);
        Display adapter=new Display(this.getActivity(), names, imgs, rates, dates);
        itemname.clear();
        itemimg.clear();
        itemrate.clear();
        itemdate.clear();
        itemid.clear();

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
