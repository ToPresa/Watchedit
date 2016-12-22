package com.watchedit.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationSeries extends Service implements AsyncResponse{

    private static final String TAG = "HelloService";
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";
    private boolean isRunning  = false;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUser = mRootRef.child("user");

    int counter=111110;
    @Override
    public void onCreate() {
        FacebookSdk.sdkInitialize(getApplicationContext());


        isRunning = true;
    }
    public void request(String req){

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/"+req+"?api_key="+APIKEYthemovieDB+"&language=en-US");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        if (AccessToken.getCurrentAccessToken() != null) {
            mUser.child(AccessToken.getCurrentAccessToken().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //for(int i=0;i<dataSnapshot.getValue().)
                    if(dataSnapshot.getValue()!=null) {
                        String a = dataSnapshot.getValue().toString().replace("{", "");
                        String b = a.replace("}", "");
                        String c[] = b.split(", ");
                            for (int i = 0; i < c.length; i++) {
                                String d[] = c[i].split("=");
                                request(d[1]);


                            }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("tt", "onCancelled", databaseError.toException());
                }
            });
        }

        return Service.START_STICKY;
    }
    @Override
    public void processFinish(String asyncresult){
counter++;
        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.

        try {
            JSONObject json = new JSONObject(asyncresult);
            JSONObject json1;
            String datess;

                datess=(json.getString("last_air_date"));
                JSONArray a = json.getJSONArray("seasons");
                for(int i=0;i<a.length();i++){
                    json1= a.getJSONObject(i);
                    if(datess.equals(json1.getString("air_date"))){
                        sendNotification(counter,json.getString("name"),json.getString("id"));
                    }
                }

        }catch(Exception e){
            return;
        }



    }

    public void sendNotification(int x,String name, String id){
        Log.i(TAG, "Service onCreate");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setContentTitle(getString(R.string.newseason));
        mBuilder.setContentText(getString(R.string.season)+" "+name);
        Intent resultIntent = new Intent(this, TvShowInf.class);
        resultIntent.putExtra("nameTvShow", id);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AndroidNavDrawerActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(x, mBuilder.build());
    }
    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
    }
}