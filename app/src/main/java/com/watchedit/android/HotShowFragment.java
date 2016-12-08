package com.watchedit.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HotShowFragment extends Fragment implements AsyncResponse {

    ListView list;
    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War",
            "Cold War"
    };

    Integer[] imgid={
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu,
            R.drawable.menu
    };
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";
    public HotShowFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        APIcall call = new APIcall();
        call.delegate=this;
       // call.execute("https://api.themoviedb.org/3/tv/popular?api_key="+APIKEYthemovieDB+"&language=en-US");

    }

    @Override
    public void processFinish(String asyncresult){
        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.
        //TextView myAwesomeTextView = (TextView)findViewById(R.id.textView);
        //myAwesomeTextView.setText(asyncresult);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_hot_show, container, false);
                list=(ListView)view.findViewById(R.id.list);
        Display adapter=new Display(this.getActivity(), itemname, imgid);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
}
