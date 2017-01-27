package com.example.rafal.movieapp.adapter.trailerAdapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.R;

/**
 * Created by rafal on 05.01.2017.
 */

public class TrailerCursorAdapter extends CursorAdapter {

    public TrailerCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_trailers,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageButton = (ImageView) view.findViewById(R.id.play_button);
        TextView nameTrailerTextView = (TextView) view.findViewById(R.id.name_trailer_textview);
        TextView contentTrailerTextView = (TextView) view.findViewById(R.id.content_trailer);
        contentTrailerTextView.setText(cursor.getString(cursor.getColumnIndex(MovieContract.VideoMovie.COLUMN_KEY)));
        nameTrailerTextView.setText("Trailer "+(cursor.getPosition()+1));

    }
}
