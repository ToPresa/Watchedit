package com.watchedit.android;

        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import java.io.InputStream;

public class Display extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] imgid;
    private final String[] rates;
    private final String[] dates;


    public Display(Activity context, String[] itemname, String[] imgid, String[] rated, String[] dates) {
        super(context, R.layout.list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.rates=rated;
        this.dates=dates;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        RatingBar ratess = (RatingBar) rowView.findViewById(R.id.ratingBar);

        txtTitle.setText(itemname[position]);
        Picasso.with(context).load(imgid[position]).into(imageView);
        extratxt.setText(dates[position]);
        ratess.setRating(Float.parseFloat(rates[position])*5/8);
        return rowView;

    };
}