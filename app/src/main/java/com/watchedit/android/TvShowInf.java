package com.watchedit.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TvShowInf extends AppCompatActivity implements AsyncResponse{

    String name, id, rate, image, description, numberseason, year, lastepisode;
    int episodecount=0;
    List<String> genres = new ArrayList<String>();
    List<String> banners = new ArrayList<String>();
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
        ImageView img2 = (ImageView) findViewById(R.id.downarrow);
        ImageView img3 = (ImageView) findViewById(R.id.downarrow2);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                onBackPressed();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                Intent intent = new Intent(TvShowInf.this, Episodes.class);
                TvShowInf.this.startActivity(intent);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(TvShowInf.this, Banners.class);
                TvShowInf.this.startActivity(intent);

            }
        });

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tv_show_inf, container);
    }

    public String randomBanner(List<String> banners) {

        String choosen=null;
        int x=0;
        if(banners.size() >= 3) {
            for(int j=0; j < banners.size(); j++) {
                double z = 0 + (Math.random() * (banners.size() - 0));
                choosen = banners.get((int)z)+";";
                banners.remove((int)z);
                x++;
                if(x==3)
                    return choosen;
            }
        }
        else{
            for(int i=0; i < banners.size(); i++) {
                choosen = banners.get(i)+";";
            }
        }
        return choosen;
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

            MyTask call = new MyTask();
            call.execute(json);
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
        TextView desc = (TextView) findViewById(R.id.descrip);
        TextView eps = (TextView) findViewById(R.id.eps);
        TextView rec = (TextView) findViewById(R.id.recommend);
        TextView bannerss = (TextView) findViewById(R.id.banners);
        ImageView banner1 = (ImageView) findViewById(R.id.banner1);
        ImageView banner2 = (ImageView) findViewById(R.id.banner2);
        ImageView banner3 = (ImageView) findViewById(R.id.banner3);

        desc.setTypeface(null, Typeface.BOLD);
        eps.setTypeface(null, Typeface.BOLD);
        rec.setTypeface(null, Typeface.BOLD);
        bannerss.setTypeface(null, Typeface.BOLD);

        generos = new String[ genres.size() ];
        genres.toArray( generos );
       /* String randomBanners = randomBanner(banners);
        String[] banner = randomBanners.split(";");
        for(int i=0; i< banner.length; i++) {
            if(i==0)
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500"+banner[i]).into(banner1);
            else if(i==1)
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500"+banner[i]).into(banner2);
            else if(i==2)
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500"+banner[i]).into(banner3);
        }*/


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

    private class MyTask extends AsyncTask<JSONObject, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(JSONObject... params) {
            // get the string from params, which is an array

            JSONArray a = null;
            try {
                a = params[0].getJSONArray("genres");
                for (int i = 0; i < a.length(); ++i) {
                    params[0] = a.getJSONObject(i);
                    genres.add(((String) (params[0].getString("name"))));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


           /* JSONArray a2 = null;
            try {
                a2 = params[0].getJSONArray("seasons");
                for (int i = 0; i < a2.length(); ++i) {
                    params[0] = a2.getJSONObject(i);
                    banners.add(((String) (params[0].getString("poster_path"))));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }

}
