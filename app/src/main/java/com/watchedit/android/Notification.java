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

public class Notification extends Service implements AsyncResponse{

    private static final String TAG = "HelloService";
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";
    private boolean isRunning  = false;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUser = mRootRef.child("user");
    List<String> itemname = new ArrayList<String>();
    List<String> itemid = new ArrayList<String>();
    @Override
    public void onCreate() {
        FacebookSdk.sdkInitialize(getApplicationContext());


        isRunning = true;
    }
    public void request(){

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/airing_today?api_key="+APIKEYthemovieDB+"&language=en-US&page=1");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        if (AccessToken.getCurrentAccessToken() != null) {
            request();
        }

        return Service.START_STICKY;
    }
    @Override
    public void processFinish(String asyncresult){

        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.

        try {
            JSONObject json = new JSONObject(asyncresult);
            JSONArray a = json.getJSONArray("results");
            for (int i = 0; i < a.length(); ++i) {
                json = a.getJSONObject(i);
                itemname.add((String) (json.getString("name")));
                itemid.add((String) (json.getString("id")));
            }

        }catch(Exception e){
            return;
        }

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
                        if(itemid.contains(d[1])){
                            sendNotification(d[1],itemid.indexOf(d[1]));
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("tt", "onCancelled", databaseError.toException());
            }
        });

    }

    public void sendNotification(String i, int x){
        Log.i(TAG, "Service onCreate");
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    mBuilder.setSmallIcon(R.drawable.icon);
    mBuilder.setContentTitle(getString(R.string.newep));
    mBuilder.setContentText(getString(R.string.ep)+" "+itemname.get(x));
    Intent resultIntent = new Intent(this, TvShowInf.class);
    resultIntent.putExtra("nameTvShow", i);
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