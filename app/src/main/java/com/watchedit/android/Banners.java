package com.watchedit.android;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Banners extends AppCompatActivity implements AsyncResponse{
String id;
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_banners);
        Intent intent = getIntent();
        id= intent.getExtras().getString("id");

        TextView txtBanners = (TextView) findViewById(R.id.textView);
        txtBanners.setTypeface(null, Typeface.BOLD);

        addListenerOnButton();

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/"+id+"/similar?api_key="+APIKEYthemovieDB+"&language=en-US");
    }
    @Override
    public void processFinish(String asyncresult) {
        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.
        Log.e("qq",asyncresult);
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
}
