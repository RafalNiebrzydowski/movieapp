package com.example.rafal.movieapp.adapter.episodeAdapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.data.MovieContract;

/**
 * Created by rafal on 19.01.2017.
 */

public class EpisodeCursorAdapter extends CursorAdapter {

    public EpisodeCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_episode,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleEpisodeTextView = (TextView) view.findViewById(R.id.name_episode_textview);
        TextView dateEpisodeTextView = (TextView) view.findViewById(R.id.datarelease_episode_textview);
        TextView numberEpisodeTextView = (TextView) view.findViewById(R.id.episode_number);

        numberEpisodeTextView.setText("Episode " + (cursor.getPosition()+1));
        dateEpisodeTextView.setText(cursor.getString(cursor.getColumnIndex(MovieContract.Epsiode.COLUMN_DATERELEASE)));
        titleEpisodeTextView.setText(cursor.getString(cursor.getColumnIndex(MovieContract.Epsiode.COLUMN_TITLE)));
    }
}