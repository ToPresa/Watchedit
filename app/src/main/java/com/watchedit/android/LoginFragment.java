package com.watchedit.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginFragment extends Fragment {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    private String name="";
    private AccessTokenTracker accessTokenTracker;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user clicks on facebook logout

                    getActivity().setTitle(getString(R.string.title_section1));
                    ImageView xx = (ImageView) view.findViewById(R.id.imageView2);
                    TextView xc= (TextView) view.findViewById(R.id.textView);
                    xx.setVisibility(View.GONE);
                    xc.setVisibility(View.GONE);
                }
            }
        };
        if(AccessToken.getCurrentAccessToken()!=null) {


           /* mRootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.child(AccessToken.getCurrentAccessToken().getToken());

                    Log.e("test",dataSnapshot.toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
*/
            GraphRequest request1 = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    // Get facebook data from login
                    Bundle bFacebookData = getFacebookData(object);
                    getActivity().setTitle("Welcome, "+bFacebookData.get("first_name"));
                    ImageView xx = (ImageView) view.findViewById(R.id.imageView2);
                    Picasso.with(getContext()).load(bFacebookData.getString("profile_pic")).into(xx);
                    TextView xc= (TextView) view.findViewById(R.id.textView);
                    xc.setText("Hello, "+bFacebookData.getString("first_name")+" "+bFacebookData.getString("last_name"));
                    xx.setVisibility(View.VISIBLE);
                    xc.setVisibility(View.VISIBLE);
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
            request1.setParameters(parameters);
            request1.executeAsync();
        }
        else{
        }
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        progressDialog.hide();
                        Bundle bFacebookData = getFacebookData(object);
                        ImageView xx= (ImageView) view.findViewById(R.id.imageView2);
                        Picasso.with(getContext()).load(bFacebookData.getString("profile_pic")).into(xx);
                        TextView xc= (TextView) view.findViewById(R.id.textView);
                        xc.setText("Hello, "+bFacebookData.getString("first_name")+" "+bFacebookData.getString("last_name"));
                        ((AndroidNavDrawerActivity) getActivity())
                                .setActionBarTitle("Welcome, "+bFacebookData.getString("first_name"));
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("qq","vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                Log.e("qq","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("qq","qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
                Log.e("qq",exception.toString());
                Log.e("qq","zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
            }
        });

        return view;
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        }
        catch(JSONException e) {
            Log.d("qq","Error parsing JSON");
        }
        return null;
    }
    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {


            name=currentAccessToken.toString();

        } else {

            name=getString(R.string.title_section1);
        }
    }

}
