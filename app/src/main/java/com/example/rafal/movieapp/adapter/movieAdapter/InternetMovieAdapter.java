package com.example.rafal.movieapp.adapter.movieAdapter;

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
import com.example.rafal.movieapp.data.model.Movie;
import com.example.rafal.movieapp.data.MovieContract;

import java.util.List;

/**
 * Created by rafal on 13.01.2017.
 */

public class InternetMovieAdapter extends RecyclerView.Adapter<InternetMovieAdapter.MovieViewHolder> {
    private List<Movie> movieList;
    private Context mContext;

    final private MovieAdapterOnClickHandler mClickHandler;


    public interface MovieAdapterOnClickHandler {
        void onClickMovie(long _id);
    }

    public InternetMovieAdapter(Context mContext, MovieAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item_list_online, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    public void setMovies(List<Movie> movies) {
        if (movieList == null)
            movieList = movies;
        else {
            if (movies!=null)
            movieList.addAll(movies);
        }
    }

    public void addMovies(List<Movie> movies) {
        movieList.addAll(movies);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        holder.titleMovieTextView.setText(movie.getTitle());
        Glide.with(mContext)
                .load(movie.getImage()).placeholder(R.drawable.blank)
                .into(holder.posterMovieImageView);
        Cursor cursor = mContext.getContentResolver().query(MovieContract.Movie.buildMovieWithID(movie.getId()), null, null, null, null);
        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_FAVORITE)) == 0) {
                holder.favoriteMovieImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.nofavorite));

            } else
                holder.favoriteMovieImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));
        } else {
            holder.favoriteMovieImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.nofavorite));

        }
    }

    public void clearCursor() {
        movieList.clear();
    }

    @Override
    public int getItemCount() {
        if (movieList == null)
            return 0;
        else
            return movieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView titleMovieTextView;
        final ImageView posterMovieImageView;
        final ImageView favoriteMovieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleMovieTextView = (TextView) itemView.findViewById(R.id.title_movie_textView);
            posterMovieImageView = (ImageView) itemView.findViewById(R.id.poster_movie_imageView);
            favoriteMovieImageView = (ImageView) itemView.findViewById(R.id.favorite);
            posterMovieImageView.setOnClickListener(this);
            favoriteMovieImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Movie movie = movieList.get(pos);
                    Cursor cursor = mContext.getContentResolver().query(MovieContract.Movie.buildMovieWithID(movie.getId()), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        if (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_FAVORITE)) == 0) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 1);
                            String selection = MovieContract.Movie.COLUMN_ID + "= ?";
                            Utility.addToFavoriteFromInternet(mContext, Long.parseLong(Integer.toString(movie.getId())), view, contentValues, selection, MovieContract.Movie.CONTENT_URI, true);
                            favoriteMovieImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));

                        } else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 0);
                            String selection = MovieContract.Movie.COLUMN_ID + "= ? AND " + MovieContract.Movie.COLUMN_POPULAR + " = 0 " + " AND " + MovieContract.Movie.COLUMN_TOPRATED + " = 0";
                            String selection2 = MovieContract.Movie.COLUMN_ID + "= ?";
                            Utility.removeFromFavoriteFromInternet(mContext, Long.parseLong(Integer.toString(movie.getId())), view, contentValues, selection, selection2, MovieContract.Movie.CONTENT_URI);
                            favoriteMovieImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.nofavorite));
                        }

                    } else {
                        Utility.addToFavoriteFromInternet(mContext, Long.parseLong(Integer.toString(movie.getId())), view, null, null, MovieContract.Movie.CONTENT_URI, true);
                        favoriteMovieImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));

                    }

                }
            });

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movieList.get(adapterPosition);
            long _id = movie.getId();
            mClickHandler.onClickMovie(_id);
        }
    }
}
