package com.example.rafal.movieapp.utility;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.TabActivity;
import com.example.rafal.movieapp.adapter.movieAdapter.InternetMovieAdapter;
import com.example.rafal.movieapp.adapter.movieAdapter.MovieAdapter;
import com.example.rafal.movieapp.adapter.serialAdapter.InternetSerialAdapter;
import com.example.rafal.movieapp.adapter.serialAdapter.SerialAdapter;
import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.data.model.Episode;
import com.example.rafal.movieapp.data.model.Movie;
import com.example.rafal.movieapp.data.model.Serial;
import com.example.rafal.movieapp.sync.task.FetchImageTask;
import com.example.rafal.movieapp.sync.task.movieTask.FetchInternetMovieIdTask;
import com.example.rafal.movieapp.sync.task.serialTask.FetchInternetSerialIdTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by rafal on 13.01.2017.
 */

public class Utility {

    public static String addMinsToDuration(String str) {
        return str + " mins";
    }

    public static String getYearFromDate(String date) {

        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);

            calendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.toString(calendar.get(calendar.YEAR));
    }

    public static String voteAvarage(String voteAvarage) {
        return voteAvarage + "/10";
    }

    public static String voteCount(String voteAvarage) {
        return voteAvarage + " votes";
    }

    public static void addToFavorite(final Context mContext, String _id, View view, ContentValues values, String s, Uri contentUri) {

        String[] selectionArgs = {_id};
        int uri = mContext.getContentResolver().update(contentUri, values, s, selectionArgs);
        if (uri != 0) {
            Snackbar snackbar;
            snackbar = Snackbar.make(view, "Added movie to favorite", Snackbar.LENGTH_LONG).setAction("Favorite", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TabActivity myActivity = (TabActivity) mContext;
                    ((TabActivity) mContext).switchTab(1);

                }
            });
            View snackBarView = snackbar.getView();
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = mContext.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int color = typedValue.data;
            snackBarView.setBackgroundColor(color);
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
            snackbar.show();
        }
    }

    public static void removeFromFavorite(final Context mContext, String mCursor, View view, ContentValues values, String s, Uri contentUri) {

        String[] selectionArgs = {mCursor};
        int uri = mContext.getContentResolver().update(contentUri, values, s, selectionArgs);
        if (uri != 0) {
            Snackbar snackbar;
            snackbar = Snackbar.make(view, "Removed movie from favorite", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = mContext.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int color = typedValue.data;
            snackBarView.setBackgroundColor(color);
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
            snackbar.show();

        }
    }

    public static void addToFavoriteFromInternet(final Context mContext, Long _id, View view, ContentValues values, String selection, Uri contentUri, boolean isMovie) {
        if (isMovie) {
            Cursor cursor = mContext.getContentResolver().query(MovieContract.Movie.buildMovieWithID(_id), null, null, null, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_FAVORITE)) == 0) {

                    String[] selectionArgs = {Long.toString(_id)};
                    mContext.getContentResolver().update(contentUri, values, selection, selectionArgs);
                }
            } else {

                FetchInternetMovieIdTask fetchMovieIdTask = new FetchInternetMovieIdTask(mContext, Integer.parseInt(Long.toString(_id)));
                Movie movie = null;
                try {
                    movie = fetchMovieIdTask.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.Movie.COLUMN_ID, _id);
                contentValues.put(MovieContract.Movie.COLUMN_TITLE, movie.getTitle());
                contentValues.put(MovieContract.Movie.COLUMN_OVERVIEW, movie.getOverview());
                contentValues.put(MovieContract.Movie.COLUMN_DATERELEASE, movie.getDateRelease());
                contentValues.put(MovieContract.Movie.COLUMN_DURATION, movie.getDuration());
                contentValues.put(MovieContract.Movie.COLUMN_VOTECOUNT, movie.getVoteCount());
                contentValues.put(MovieContract.Movie.COLUMN_VOTEAVERAGE, movie.getVoteAverage());
                FetchImageTask fetchImageTask = new FetchImageTask(mContext);
                byte[] image = null;
                try {
                    image = fetchImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movie.getImage()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                contentValues.put(MovieContract.Movie.COLUMN_IMAGE, image);
                contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 1);
                contentValues.put(MovieContract.Movie.COLUMN_POPULAR, 0);
                contentValues.put(MovieContract.Movie.COLUMN_TOPRATED, 0);
                Uri uri = mContext.getContentResolver().insert(contentUri, contentValues);
                for (int i = 0; i < movie.getVideo().size(); i++) {
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put(MovieContract.VideoMovie.COLUMN_KEY, movie.getVideo().get(i));
                    contentValues2.put(MovieContract.VideoMovie.COLUMN_ID, _id);
                    mContext.getContentResolver().insert(MovieContract.VideoMovie.CONTENT_URI, contentValues2);
                }
                mContext.getContentResolver().notifyChange(MovieContract.Movie.CONTENT_URI, null);
            }
        } else {
            Cursor cursor = mContext.getContentResolver().query(MovieContract.Serial.buildSerialWithID(_id), null, null, null, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_FAVORITE)) == 0) {

                    String[] selectionArgs = {Long.toString(_id)};
                    mContext.getContentResolver().update(contentUri, values, selection, selectionArgs);
                }
            } else {

                FetchInternetSerialIdTask fetchMovieIdTask2 = new FetchInternetSerialIdTask(mContext, Integer.parseInt(Long.toString(_id)));
                Serial serial = null;
                try {
                    serial = fetchMovieIdTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.Serial.COLUMN_ID, _id);
                contentValues.put(MovieContract.Serial.COLUMN_TITLE, serial.getTitle());
                contentValues.put(MovieContract.Serial.COLUMN_OVERVIEW, serial.getOverview());
                contentValues.put(MovieContract.Serial.COLUMN_DATERELEASE, serial.getDateRelease());
                contentValues.put(MovieContract.Serial.COLUMN_VOTECOUNT, serial.getVoteCount());
                contentValues.put(MovieContract.Serial.COLUMN_VOTEAVERAGE, serial.getVoteAverage());
                FetchImageTask fetchImageTask = new FetchImageTask(mContext);
                byte[] image = null;
                try {
                    image = fetchImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,serial.getImage()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                contentValues.put(MovieContract.Serial.COLUMN_IMAGE, image);
                contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 1);
                contentValues.put(MovieContract.Serial.COLUMN_POPULAR, 0);
                contentValues.put(MovieContract.Serial.COLUMN_TOPRATED, 0);
                Uri uri = mContext.getContentResolver().insert(contentUri, contentValues);
                contentValues.clear();
                for (int i = 0; i < serial.getSeasons().size(); i++) {
                    contentValues.put(MovieContract.Season.COLUMN_ID, _id);
                    contentValues.put(MovieContract.Season.COLUMN_NUMBER, serial.getSeasons().get(i).getNumberSeason());
                    Uri uriSeason = mContext.getContentResolver().insert(MovieContract.Season.CONTENT_URI, contentValues);
                    if (serial.getSeasons().get(i).getEpisodes() != null)
                        for (Episode e : serial.getSeasons().get(i).getEpisodes()) {
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put(MovieContract.Epsiode.COLUMN_ID, MovieContract.Season.getIdFromUri(uriSeason));
                            contentValues1.put(MovieContract.Epsiode.COLUMN_DATERELEASE, e.getDate());
                            contentValues1.put(MovieContract.Epsiode.COLUMN_TITLE, e.getTitle());
                            mContext.getContentResolver().insert(MovieContract.Epsiode.CONTENT_URI, contentValues1);
                        }
                    contentValues.clear();
                }
                mContext.getContentResolver().notifyChange(MovieContract.Serial.CONTENT_URI, null);
            }
        }


        Snackbar snackbar;
        snackbar = Snackbar.make(view, "Added movie to favorite", Snackbar.LENGTH_LONG).setAction("Favorite", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity myActivity;
                try {
                    myActivity = (TabActivity) mContext;
                    ((TabActivity) mContext).switchTab(1);
                }
                catch (ClassCastException e){
                    Bundle b = new Bundle();
                    b.putInt("pos", 1);
                    mContext.startActivity(new Intent(v.getContext(), TabActivity.class).putExtras(b));
                }
            }
        });
        View snackBarView = snackbar.getView();
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
        snackBarView.setBackgroundColor(color);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        snackbar.show();


    }

    public static void removeFromFavoriteFromInternet(final Context mContext, Long _id, View view, ContentValues values, String s, String s2, Uri contentUri) {


        String[] selectionArgs = {Long.toString(_id)};
        int uri = mContext.getContentResolver().delete(contentUri, s, selectionArgs);
        if (uri == 0) {

            mContext.getContentResolver().update(contentUri, values, s2, selectionArgs);
        }
        Snackbar snackbar;
        snackbar = Snackbar.make(view, "Removed movie from favorite", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
        snackBarView.setBackgroundColor(color);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        snackbar.show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static float getPrefferedItemHeight(final Activity activity){
        android.util.TypedValue value = new android.util.TypedValue();
        boolean b = activity.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, value, true);
        String s = TypedValue.coerceToString(value.type, value.data);
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return value.getDimension(metrics);

    }

    public static Button generateButton(Activity activity, int i){
        Button button = new Button(activity);
        button.setBackgroundResource(R.color.colorAccent);
        button.setGravity(Gravity.CENTER);
        button.setPadding(0, 16, 0, 16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 16, 0, 0);
        button.setLayoutParams(params);

        button.setText("Season " + (i + 1));
        return button;
    }

    public static void showViews(RecyclerView recyclerView, ProgressBar progressBar){
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public static void showLoading(RecyclerView recyclerView, ProgressBar progressBar){
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public static <T> void configureRecyclerView(RecyclerView recyclerView, Context context, T adapter) {
        GridLayoutManager layoutManager =
                new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        if(adapter instanceof MovieAdapter)
            recyclerView.setAdapter((MovieAdapter)adapter);
        if(adapter instanceof SerialAdapter)
            recyclerView.setAdapter((SerialAdapter)adapter);
        if(adapter instanceof InternetMovieAdapter)
            recyclerView.setAdapter((InternetMovieAdapter)adapter);
        if(adapter instanceof InternetSerialAdapter)
            recyclerView.setAdapter((InternetSerialAdapter)adapter);
    }

    public static <T> void configureVerticalRecyclerView(RecyclerView recyclerView, Context context, T adapter) {
        GridLayoutManager layoutManager =
                new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        if(adapter instanceof InternetMovieAdapter)
            recyclerView.setAdapter((InternetMovieAdapter)adapter);
        if(adapter instanceof InternetSerialAdapter)
            recyclerView.setAdapter((InternetSerialAdapter)adapter);
    }
}
