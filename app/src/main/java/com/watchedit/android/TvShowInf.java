package com.watchedit.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TvShowInf extends AppCompatActivity implements AsyncResponse{

    String name, id, rate, image, description, numberseason, year, lastepisode, back;
    int episodecount=0;
    List<String> genres = new ArrayList<String>();
    List<String> banners = new ArrayList<String>();
    String[] generos;
    String[] bannerfinal;
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";
    private  ImageView img22;

    private ProgressDialog simpleWaitDialog;

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
        Button btn1 = (Button) findViewById(R.id.button);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);

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

        btn1.setOnClickListener(new View.OnClickListener() {

            ImageView downloadedImg = (ImageView) findViewById(R.id.banner1);
            @Override
            public void onClick(View v) {
                // Execute DownloadImage AsyncTask
                new DownloadFile().execute("https://image.tmdb.org/t/p/w500/vxuoMW6YBt6UsxvMfRNwRl9LtWS.jpg");
            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {

            ImageView downloadedImg = (ImageView) findViewById(R.id.banner2);
            @Override
            public void onClick(View v) {

                new DownloadFile().execute("https://image.tmdb.org/t/p/w500/vxuoMW6YBt6UsxvMfRNwRl9LtWS.jpg");
            }


        });

        btn3.setOnClickListener(new View.OnClickListener() {

            ImageView downloadedImg = (ImageView) findViewById(R.id.banner3);
            @Override
            public void onClick(View v) {

                new DownloadFile().execute("https://image.tmdb.org/t/p/w500/vxuoMW6YBt6UsxvMfRNwRl9LtWS.jpg");
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
                choosen += banners.get((int)z)+";";
                banners.remove((int)z);
                x++;
                if(x==3)
                    return choosen;
            }
        }
        else{
            for(int i=0; i < banners.size(); i++) {
                choosen += banners.get(i)+";";
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
            back = json.getString("backdrop_path");

            //MyTask call = new MyTask();
            //call.execute(json);
            JSONArray a = null;
            try {
                a = json.getJSONArray("genres");
                for (int i = 0; i < a.length(); ++i) {
                    genres.add(((String) (a.getJSONObject(i).getString("name"))));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("sdd",json.toString());
            JSONArray a2 = null;
            a2 = json.getJSONArray("seasons");
            Log.e("sdd",a2.toString());
            try {

                for (int i = 0; i < a2.length(); ++i) {
                    banners.add(((String) (a2.getJSONObject(i).getString("poster_path"))));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        Button btn1 = (Button) findViewById(R.id.button);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);

        desc.setTypeface(null, Typeface.BOLD);
        eps.setTypeface(null, Typeface.BOLD);
        rec.setTypeface(null, Typeface.BOLD);
        bannerss.setTypeface(null, Typeface.BOLD);

        generos = new String[ genres.size() ];
        genres.toArray( generos );
        String randomBanners = randomBanner(banners);
        String[] banner = randomBanners.split(";");
        bannerfinal = banner;
        for(int i=0; i< banner.length; i++) {
            if (i == 0) {
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + banner[i]).into(banner1);
                btn1.setVisibility(View.VISIBLE);
            } else if (i == 1) {
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + banner[i]).into(banner2);
                btn2.setVisibility(View.VISIBLE);
            } else if (i == 2) {
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + banner[i]).into(banner3);
                btn3.setVisibility(View.VISIBLE);
            }
        }

        img22= (ImageView) findViewById(R.id.imageView3);
        img22.setScaleType(ImageView.ScaleType.FIT_XY);

        Picasso.with(this).load("https://image.tmdb.org/t/p/w500"+back).into(img22);
        Picasso.with(this).load("https://image.tmdb.org/t/p/w500"+banners.get(0)).into(banner1);
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

    class DownloadFile extends AsyncTask<String,Integer,Long> {
        ProgressDialog mProgressDialog = new ProgressDialog(TvShowInf.this);// Change Mainactivity.this with your activity name.
        String strFolderName;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Downloading");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }
        @Override
        protected Long doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                String targetFileName="Name"+".rar";//Change name and subname
                int lenghtOfFile = conexion.getContentLength();
                String PATH = Environment.getExternalStorageDirectory()+ "/";
                File folder = new File(PATH);
                if(!folder.exists()){
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH+targetFileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress ((int)(total*100/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;
        }
        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setProgress(progress[0]);
            if(mProgressDialog.getProgress()==mProgressDialog.getMax()){
                mProgressDialog.dismiss();
            }
        }
        protected void onPostExecute(String result) {
        }
    }
}


