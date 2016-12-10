package com.watchedit.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TvShowInf extends AppCompatActivity implements AsyncResponse{

    String name, id, rate, image, description, numberseason, year, lastepisode;
    int episodecount=0;
    List<String> genres = new ArrayList<String>();
    String[] generos;
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        id= intent.getExtras().getString("nameTvShow");

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/"+id+"?api_key="+APIKEYthemovieDB+"&language=en-US");

        setContentView(R.layout.activity_tv_show_inf);

        addListenerOnButton();

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tv_show_inf, container);
    }

    @Override
    public void processFinish(String asyncresult){
        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.

        try {
            JSONObject json = new JSONObject(asyncresult);
            name = ((String) (json.getString("original_name")));
            image = ("https://image.tmdb.org/t/p/w500"+(json.getString("poster_path")));
            description = ((String) (json.getString("overview")));
            episodecount = Integer.parseInt((json.getString("number_of_episodes")));
            numberseason = ((String) (json.getString("number_of_seasons")));
            rate = (String) (json.getString("vote_average"));
            year = (String) (json.getString("first_air_date"));
            lastepisode = (String) (json.getString("last_air_date"));

            JSONArray a = json.getJSONArray("genres");
            for (int i = 0; i < a.length(); ++i) {
                json = a.getJSONObject(i);
                genres.add(((String) (json.getString("name"))));
            }

            Log.e("asdasadsdasdadasd",name);
        }catch(Exception e){
            return;
        }

        TextView txtTitle = (TextView) findViewById(R.id.item);
        ImageView imageView = (ImageView) findViewById(R.id.icon);
        TextView txtnumberseason = (TextView) findViewById(R.id.seasons);
        TextView txtepisodecount = (TextView) findViewById(R.id.episodes);
        RatingBar rates = (RatingBar) findViewById(R.id.ratingBar);
        TextView years = (TextView) findViewById(R.id.year);
        TextView episodelast = (TextView) findViewById(R.id.lastepisode);
        TextView genress = (TextView) findViewById(R.id.genre);
        TextView descriptions = (TextView) findViewById(R.id.description);

        generos = new String[ genres.size() ];
        genres.toArray( generos );

        txtTitle.setText(name);
        Picasso.with(this).load(image).into(imageView);
        txtnumberseason.setText(numberseason+" seasons");
        txtepisodecount.setText(String.valueOf(episodecount) + " episodes");
        rates.setRating(Float.parseFloat(rate)*5/8);
        years.setText("Realese Year " + year);
        episodelast.setText("Last Episode: "+lastepisode);
        genress.setText("Genres: "+ Arrays.toString(generos));
        descriptions.setText(description);

    }

}
