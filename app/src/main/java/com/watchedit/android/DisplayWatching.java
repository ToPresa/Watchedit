package com.watchedit.android;

/**
 * Created by topre on 20/12/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class DisplayWatching extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] imgid;
    private final String[] rates;
    private final String[] dates;
    private final String[] ids;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUser = mRootRef.child("user");


    public DisplayWatching(Activity context, String[] itemname, String[] imgid, String[] rated, String[] dates, String[] ids) {
        super(context, R.layout.list, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
        this.rates = rated;
        this.dates = dates;
        this.ids=ids;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listwatching, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        RatingBar ratess = (RatingBar) rowView.findViewById(R.id.ratingBar);
        ImageView imgb = (ImageView) rowView.findViewById(R.id.imageView52);

        txtTitle.setText(itemname[position]);
        imgb.setTag(ids[position]);
        Picasso.with(context).load(imgid[position]).into(imageView);
        extratxt.setText(dates[position]);
        ratess.setRating(Float.parseFloat(rates[position]) * 5 / 8);
        imgb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View xx=v;
                mUser.child(AccessToken.getCurrentAccessToken().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //for(int i=0;i<dataSnapshot.getValue().)
                        String a=dataSnapshot.getValue().toString().replace("{","");
                        String b=a.replace("}","");
                        String c[]=b.split(", ");
                        for(int i=0; i<c.length;i++){
                            String d[]=c[i].split("=");
                            if(d[1].equals(xx.getTag())){
                                ((ViewGroup) xx.getParent()).removeAllViews();
                                mUser.child(AccessToken.getCurrentAccessToken().getToken()).child(d[0]).removeValue();
                            }
                            Log.e("wew",d[0]);
                            Log.e("wew",d[1]);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("tt", "onCancelled", databaseError.toException());
                    }
                });
            }
        });
        return rowView;

    }


}
