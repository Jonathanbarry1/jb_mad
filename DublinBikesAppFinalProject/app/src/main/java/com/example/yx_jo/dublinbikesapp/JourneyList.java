package com.example.yx_jo.dublinbikesapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yx_jo on 01/10/2017.
 */

public class JourneyList extends ArrayAdapter<Journey> {

    private Activity context;
    private List<Journey> journeyList;

    public JourneyList(Activity context, List<Journey> journeyList){
        super(context, R.layout.list_layout, journeyList);
        this.context = context;
        this.journeyList = journeyList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewStart = (TextView) listViewItem.findViewById(R.id.textViewStartStation);
        TextView textViewEnd = (TextView) listViewItem.findViewById(R.id.textViewEndStation);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
        ImageView imageView2 = (ImageView) listViewItem.findViewById(R.id.imageView2);

        Journey journey = journeyList.get(position);


        Date date = journey.getDate();
        String formattedDate = DateFormat.getDateTimeInstance().format(date);


        String start = context.getResources().getString(R.string.StartStation) + " " + journey.getStartName();
        String end = context.getResources().getString(R.string.FinishStation) + " " + journey.getEndName();

        textViewStart.setText(start);
        textViewEnd.setText(end);
        textViewDate.setText(formattedDate);
        Picasso.with(context).load(journey.getImageURL()).fit().centerCrop().into(imageView2);
        return listViewItem;

    }
}
