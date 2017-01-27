package com.example.rafal.movieapp.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.data.MovieContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by rafal on 13.01.2017.
 */

public class MovieSyncUtils {


    private static boolean sInitialized;
    public static final String ACTION_DATA_UPDATED =
            "com.example.rafal.movieapp.ACTION_DATA_UPDATED";
    private static final String MOVIE_SYNC_TASK = "movie-sync";


    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int SYNC_INTERVAL_HOURS = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.pref_key_sync), context.getString(R.string.pref_default_sync)));
        if (SYNC_INTERVAL_HOURS != -1) {
            int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
            int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
            Driver driver = new GooglePlayDriver(context);
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

            Job syncMovieJob = dispatcher.newJobBuilder()
                    .setService(MovieFirebaseJobService.class)
                    .setTag(MOVIE_SYNC_TASK)
                    //Change it - wifi
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(
                            SYNC_INTERVAL_SECONDS,
                            SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                    .setReplaceCurrent(true)
                    .build();

            dispatcher.schedule(syncMovieJob);
        }
    }

    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                Uri movieQueryUri = MovieContract.Movie.CONTENT_URI;

                String selection = MovieContract.Movie.COLUMN_POPULAR + " =?";
                String[] selectionArgs = new String[]{String.valueOf(1)};
                Cursor cursor = context.getContentResolver().query(
                        movieQueryUri,
                        null,
                        selection,
                        selectionArgs,
                        null);


                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }


                cursor.close();
            }
        });

        checkForEmpty.start();
    }

    /**
     * Helper method to perform resetLoader sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }

}
