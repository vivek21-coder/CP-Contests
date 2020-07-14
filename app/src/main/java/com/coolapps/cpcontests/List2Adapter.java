package com.coolapps.cpcontests;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class List2Adapter extends ArrayAdapter {

    private final Activity context;
    private final String[] headings;
    private final Integer[] images;
    private final String[] starts;
    private final String[] ends;
    private final String[] durations;
    private final boolean[] reddots;

    public List2Adapter(Activity context, String[] headings, Integer[] images, String[] starts, String[] ends, String[] durations, boolean[] reddots) {
        super(context, R.layout.list2, headings);

        this.context = context;
        this.headings = headings;
        this.images = images;
        this.starts = starts;
        this.ends = ends;
        this.durations = durations;
        this.reddots = reddots;
    }


    public View getView(int pos, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list2, null, true);

        //this code gets references to objects in the xml file
        TextView headingView = rowView.findViewById(R.id.headingViewID);
        ImageView imageView = rowView.findViewById(R.id.imageViewID);
        TextView startView = rowView.findViewById(R.id.startViewID);
        TextView endView = rowView.findViewById(R.id.endViewID);
        TextView durationView = rowView.findViewById(R.id.durationViewID);
        RelativeLayout relativeLayout = rowView.findViewById(R.id.rlID);
        TextView liveView = rowView.findViewById(R.id.liveID);

        headingView.setText(headings[pos]);
        imageView.setImageResource(images[pos]);
        startView.setText(starts[pos]);
        endView.setText(ends[pos]);
        durationView.setText(durations[pos]);
        if (reddots[pos]) {
            relativeLayout.setBackgroundColor(Color.argb(50, 0, 255, 0));
        } else {
            liveView.setVisibility(View.GONE);
        }

        return rowView;

    }
}
