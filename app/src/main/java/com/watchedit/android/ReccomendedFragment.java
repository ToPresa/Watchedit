package com.watchedit.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


public class ReccomendedFragment extends Fragment implements AsyncResponse{
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUser = mRootRef.child("user");
    ListView list;
    List<String> itemname = new ArrayList<String>();
    List<String> itemimg = new ArrayList<String>();
    List<String> itemrate = new ArrayList<String>();
    List<String> itemdate = new ArrayList<String>();
    List<String> itemid = new ArrayList<String>();
    List<String> itemname1 = new ArrayList<String>();
    List<String> itemimg1 = new ArrayList<String>();
    List<String> itemrate1 = new ArrayList<String>();
    List<String> itemdate1= new ArrayList<String>();
    List<String> itemid1 = new ArrayList<String>();
    String[] names;
    String[] imgs;
    String[] rates;
    String[] dates;
    String[] ids;
    int length=0;
    int round=0;
    private String APIKEYthemovieDB = "cc0ee2bbfea45383a8c9381a4995aecd";
    public ReccomendedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_reccomended, container, false);
        if (AccessToken.getCurrentAccessToken() != null) {
            TextView tt=(TextView) v.findViewById(R.id.test);
            ImageView ee=(ImageView) v.findViewById(R.id.imags);
            ee.setVisibility(View.INVISIBLE);
            tt.setVisibility(View.INVISIBLE);

            mUser.child(AccessToken.getCurrentAccessToken().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //for(int i=0;i<dataSnapshot.getValue().)
                    if(dataSnapshot.getValue()!=null) {
                        String a = dataSnapshot.getValue().toString().replace("{", "");
                        String b = a.replace("}", "");
                        String c[] = b.split(", ");
                        length=c.length;
                        for (int i = 0; i < c.length; i++) {
                            String d[] = c[i].split("=");
                            //pedido
                            request(d[1]);

                        }
                    }
                    else{
                        //mensagem vazia
                        TextView tt=(TextView) v.findViewById(R.id.test);
                        tt.setText(R.string.notenough);
                        ImageView ee=(ImageView) v.findViewById(R.id.imags);
                        ee.setVisibility(View.VISIBLE);
                        tt.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("tt", "onCancelled", databaseError.toException());
                }
            });

        }
        else{
            TextView tt=(TextView) v.findViewById(R.id.test);
            tt.setText(R.string.login);
            ImageView ee=(ImageView) v.findViewById(R.id.imags);
            ee.setVisibility(View.VISIBLE);
            tt.setVisibility(View.VISIBLE);        }
        return v;
    }
    public void request(String req){

        APIcall call = new APIcall();
        call.delegate=this;
        call.execute("https://api.themoviedb.org/3/tv/"+req+"/recommendations?api_key="+APIKEYthemovieDB+"&language=en-US&page=1");
    }
    @Override
    public void processFinish(String asyncresult){
        round++;
        //This method will get call as soon as your AsyncTask is complete. asyncresult will be your result.

        try {
            JSONObject json = new JSONObject(asyncresult);
            JSONArray a = json.getJSONArray("results");
            for (int i = 0; i < a.length(); ++i) {
                json = a.getJSONObject(i);
                itemname1.add((String) (json.getString("name")));
                itemimg1.add("https://image.tmdb.org/t/p/w500"+(json.getString("poster_path")));
                itemrate1.add((String) (json.getString("vote_average")));
                itemdate1.add("First aired: "+ (json.getString("first_air_date")));
                itemid1.add((String) (json.getString("id")));

            }

        }catch(Exception e){
            return;
        }


        if(length == round) {
            Map<String, Integer> map = new HashMap<String, Integer>();

            for (String temp : itemname1) {
                Integer count = map.get(temp);
                map.put(temp, (count == null) ? 1 : count + 1);
            }
            Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);
            SortedSet<Map.Entry<String,Integer>> sortedEntries = entriesSortedByValues(treeMap);
            for(Map.Entry<String,Integer> entry : sortedEntries) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if(value>2) {
                    int ds = itemname1.indexOf(key);
                    itemname.add(itemname1.get(ds));
                    itemimg.add(itemimg1.get(ds));
                    itemrate.add(itemrate1.get(ds));
                    itemdate.add(itemdate1.get(ds));
                    itemid.add(itemid1.get(ds));
                }
                else{
break;
                }
            }
            if (itemname.size() == 0) {
                TextView tt = (TextView) getActivity().findViewById(R.id.test);
                tt.setText(R.string.notenough);
                ImageView ee = (ImageView) getActivity().findViewById(R.id.imags);
                ee.setVisibility(View.VISIBLE);
                tt.setVisibility(View.VISIBLE);
            } else {
                names = new String[itemname.size()];
                itemname.toArray(names);
                imgs = new String[itemimg.size()];
                itemimg.toArray(imgs);
                rates = new String[itemrate.size()];
                itemrate.toArray(rates);
                dates = new String[itemdate.size()];
                itemdate.toArray(dates);
                ids = new String[itemid.size()];
                itemid.toArray(ids);
                list = (ListView) getActivity().findViewById(R.id.list1121);
                Display adapter = new Display(this.getActivity(), names, imgs, rates, dates);

                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub


                        String Slecteditem = ids[+position];
                        //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                        Intent myIntent = new Intent(getActivity(), TvShowInf.class);
                        myIntent.putExtra("nameTvShow", Slecteditem);

                        getActivity().startActivity(myIntent);
                    }
                });
            }
        }
        /**/
    }
    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
