package com.watchedit.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Episodes extends AppCompatActivity implements AsyncResponse {

    String numberseaons, id;
    ListView list;
    List<String> itemname = new ArrayList<String>();
    List<String> itemepNumber = new ArrayList<String>();
    String [] names, epsnum;
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        numberseaons = getIntent().getStringExtra("countseasons");
        id = getIntent().getStringExtra("idseason");
        showButtons(Integer.parseInt(numberseaons));

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/"+id+"/season/"+"1"+"?api_key="+APIKEYthemovieDB+"&language=en-US");

        addListenerOnButton();
    }

    public void showButtons(int seasonsnumber) {
        if(1 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn1); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("1");}}); }
        if(2 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn2); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("2");}}); }
        if(3 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn3); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("3");}}); }
        if(4 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn4); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("4");}}); }
        if(5 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn5); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("5");}}); }
        if(6 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn6); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("6");}}); }
        if(7 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn7); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("7");}}); }
        if(8 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn8); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("8");}}); }
        if(9 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn9); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("9");}}); }
        if(10 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn10); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("10");}}); }
        if(11 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn11); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("11");}}); }
        if(12 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn12); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("12");}}); }
        if(13 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn13); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("13");}}); }
        if(14 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn14); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("14");}}); }
        if(15 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn15); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("15");}}); }
        if(16 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn16); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("16");}}); }
        if(17 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn17); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("17");}}); }
        if(18 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn18); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("18");}}); }
        if(19 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn19); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("19");}}); }
        if(20 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn20); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("20");}}); }
        if(21 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn21); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("21");}}); }
        if(22 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn22); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("22");}}); }
        if(23 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn23); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("23");}}); }
        if(24 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn24); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("24");}}); }
        if(25 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn25); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("25");}}); }
        if(26 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn26); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("26");}}); }
        if(27 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn27); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("27");}}); }
        if(28 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn28); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("28");}}); }
        if(29 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn29); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("29");}}); }
        if(30 <= seasonsnumber) {Button btn = (Button) findViewById(R.id.btn30); btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {request("30");}}); }
    }

    public void request(String seasonnumber) {

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/"+id+"/season/"+seasonnumber+"?api_key="+APIKEYthemovieDB+"&language=en-US");

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

    @Override
    public void processFinish(String asyncresult) {
        try {
            JSONObject json = new JSONObject(asyncresult);
            JSONArray a = json.getJSONArray("episodes");
            for (int i = 0; i < a.length(); ++i) {
                json = a.getJSONObject(i);
                itemepNumber.add(((String) (json.getString("episode_number"))));
                itemname.add(((String) (json.getString("name"))));
            }

        }catch(Exception e){
            return;
        }

        names = new String[ itemname.size() ];
        itemname.toArray( names );
        epsnum = new String[ itemepNumber.size() ];
        itemepNumber.toArray( epsnum );
        list=(ListView) findViewById(R.id.list2);
        DisplayEpisodes adapter=new DisplayEpisodes(this, names,epsnum);
        itemname.clear();
        itemepNumber.clear();
        list.setAdapter(adapter);

    }
};

