package com.example.rafal.movieapp.adapter.trailerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafal.movieapp.R;

import java.util.ArrayList;

/**
 * Created by rafal on 05.01.2017.
 */

public class TrailerAdapter extends ArrayAdapter<String> {

    public TrailerAdapter(Context context, ArrayList<String> video) {
        super(context, 0, video);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        String key_video = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trailers, parent, false);
        }
        if(key_video!=null)
        {
        ImageView imageButton = (ImageView) view.findViewById(R.id.play_button);
        TextView nameTrailerTextView = (TextView) view.findViewById(R.id.name_trailer_textview);
        TextView contentTrailerTextView = (TextView) view.findViewById(R.id.content_trailer);
        contentTrailerTextView.setText(key_video);
        nameTrailerTextView.setText("Trailer "+(position+1));
        }
        return view;
    }




}