package com.example.rafal.movieapp.adapter.serialAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.utility.Utility;
import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.data.model.Serial;

import java.util.List;

/**
 * Created by rafal on 13.01.2017.
 */

public class InternetSerialAdapter extends RecyclerView.Adapter<InternetSerialAdapter.MovieViewHolder> {
    private List<Serial> serialList;
    private Context mContext;

    final private SerialAdapterOnClickHandler mClickHandler;


    public interface SerialAdapterOnClickHandler {
        void onClickSerialI(long _id);
    }

    public InternetSerialAdapter(Context mContext, SerialAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item_list_online, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    public void setMovies(List<Serial> movies) {
        if (serialList == null)
            serialList = movies;
        else
            serialList.addAll(movies);
    }

    public void addMovies(List<Serial> movies) {
        serialList.addAll(movies);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Serial movie = serialList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        Glide.with(mContext)
                .load(movie.getImage()).placeholder(R.drawable.blank)
                .into(holder.imageView);
        Cursor cursor = mContext.getContentResolver().query(MovieContract.Serial.buildSerialWithID(movie.getId()), null, null, null, null);
        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_FAVORITE)) == 0) {
                holder.imageViewFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.nofavorite));

            } else
                holder.imageViewFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));
        } else {
            holder.imageViewFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.nofavorite));

        }
    }

    public void clearCursor() {
        serialList.clear();
    }

    @Override
    public int getItemCount() {
        if (serialList == null)
            return 0;
        else
            return serialList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView titleTextView;
        final ImageView imageView;
        final ImageView imageViewFavorite;


        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title_movie_textView);
            imageView = (ImageView) itemView.findViewById(R.id.poster_movie_imageView);
            itemView.setOnClickListener(this);
            imageViewFavorite = (ImageView) itemView.findViewById(R.id.favorite);
            imageViewFavorite.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Serial serial = serialList.get(pos);
                    Cursor cursor = mContext.getContentResolver().query(MovieContract.Serial.buildSerialWithID(serial.getId()), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        if (cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_FAVORITE)) == 0) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 1);
                            String selection = MovieContract.Serial.COLUMN_ID + "= ?";
                            Utility.addToFavoriteFromInternet(mContext, Long.parseLong(Integer.toString(serial.getId())), view, contentValues, selection, MovieContract.Serial.CONTENT_URI, false);
                            imageViewFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));

                        } else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 0);
                            String selection = MovieContract.Serial.COLUMN_ID + "= ? AND " + MovieContract.Serial.COLUMN_POPULAR + " = 0 " + " AND " + MovieContract.Serial.COLUMN_TOPRATED + " = 0";
                            String selection2 = MovieContract.Serial.COLUMN_ID + "= ?";
                            Utility.removeFromFavoriteFromInternet(mContext, Long.parseLong(Integer.toString(serial.getId())), view, contentValues, selection, selection2, MovieContract.Serial.CONTENT_URI);
                            imageViewFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.nofavorite));
                        }

                    } else {
                        Utility.addToFavoriteFromInternet(mContext, Long.parseLong(Integer.toString(serial.getId())), view, null, null, MovieContract.Serial.CONTENT_URI, false);
                        imageViewFavorite.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));

                    }

                }
            });
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Serial movie = serialList.get(adapterPosition);
            long _id = movie.getId();
            mClickHandler.onClickSerialI(_id);
        }
    }
}
