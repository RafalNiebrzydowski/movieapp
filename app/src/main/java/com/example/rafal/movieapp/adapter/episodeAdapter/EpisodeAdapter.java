package com.example.rafal.movieapp.adapter.episodeAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.data.model.Episode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafal on 19.01.2017.
 */

public class EpisodeAdapter extends ArrayAdapter<Episode> {

    List<Episode> episodeList = new ArrayList<>();

    public EpisodeAdapter(Context context, List<Episode> episodes) {
        super(context, 0, episodes);
        episodeList = episodes;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_episode, parent, false);
        }
        Episode episode = episodeList.get(position);

        if (episode != null) {
            TextView titleEpisodeTextView = (TextView) view.findViewById(R.id.name_episode_textview);
            TextView dateEpisodeTextView = (TextView) view.findViewById(R.id.datarelease_episode_textview);
            TextView numberEpisodeTextView = (TextView) view.findViewById(R.id.episode_number);

            numberEpisodeTextView.setText("Episode " + (position + 1));
            dateEpisodeTextView.setText(episode.getDate());
            titleEpisodeTextView.setText(episode.getTitle());
        }

        return view;
    }
}
