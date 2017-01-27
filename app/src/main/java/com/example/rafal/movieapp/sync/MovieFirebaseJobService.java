package com.example.rafal.movieapp.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by rafal on 13.01.2017.
 */
public class MovieFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchMovieTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mFetchMovieTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                MovieSyncTask.syncMovie(context);
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };

        mFetchMovieTask.execute();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchMovieTask != null) {
            mFetchMovieTask.cancel(true);
        }
        return true;
    }
}