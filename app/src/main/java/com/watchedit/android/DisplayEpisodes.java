package com.watchedit.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class DisplayEpisodes extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] itemep;


    public DisplayEpisodes(Activity context, String[] itemname, String[] itemep) {
        super(context, R.layout.list2, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.itemep=itemep;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list2, null,true);

        TextView txtEpNumber = (TextView) rowView.findViewById(R.id.txt1);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname[position]);
        txtEpNumber.setText(itemep[position]);
        return rowView;

    };
}
