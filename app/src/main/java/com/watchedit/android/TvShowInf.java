package com.watchedit.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.watchedit.android",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
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
                intent.putExtra("id",id);
                TvShowInf.this.startActivity(intent);

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {

            ImageView downloadedImg = (ImageView) findViewById(R.id.banner1);
            @Override
            public void onClick(View v) {
                // Execute DownloadImage AsyncTask
                DownloadFile(v,0);
            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {

            ImageView downloadedImg = (ImageView) findViewById(R.id.banner2);
            @Override
            public void onClick(View v) {

                DownloadFile(v,1);
            }


        });

        btn3.setOnClickListener(new View.OnClickListener() {

            ImageView downloadedImg = (ImageView) findViewById(R.id.banner3);
            @Override
            public void onClick(View v) {

                DownloadFile(v,2);
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
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + bannerfinal[i].replaceAll("null","")).into(banner1);
                Log.e("wee",bannerfinal[i]);
                btn1.setVisibility(View.VISIBLE);
            } else if (i == 1) {
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + bannerfinal[i]).into(banner2);
                btn2.setVisibility(View.VISIBLE);
            } else if (i == 2) {
                Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + bannerfinal[i]).into(banner3);
                btn3.setVisibility(View.VISIBLE);
            }
        }

        img22= (ImageView) findViewById(R.id.imageView3);
        img22.setScaleType(ImageView.ScaleType.FIT_XY);

        Picasso.with(this).load("https://image.tmdb.org/t/p/w500"+back).into(img22);
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

    public void DownloadFile (View v, int i){
        Picasso.with(v.getContext())
                .load("https://image.tmdb.org/t/p/w500"+(bannerfinal[i].replaceAll("null","")))
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/WatchedIt");

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }

                                  String name = new Date().toString() + ".jpg";
                                  myDir = new File(myDir, name);
                                  FileOutputStream out = new FileOutputStream(myDir);
                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                  out.flush();
                                  out.close();

                              } catch(Exception e){
                                  // some action
                              }
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );
        new AlertDialog.Builder(v.getContext())
                .setTitle("Download")
                .setMessage("Image downloaded")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}


