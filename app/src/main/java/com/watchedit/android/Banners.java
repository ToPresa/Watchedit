package com.watchedit.android;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Banners extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banners);

        TextView txtBanners = (TextView) findViewById(R.id.textView);
        txtBanners.setTypeface(null, Typeface.BOLD);

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
}
