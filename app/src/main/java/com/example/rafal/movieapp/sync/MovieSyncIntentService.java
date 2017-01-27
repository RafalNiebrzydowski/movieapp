package com.example.rafal.movieapp.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by rafal on 13.01.2017.
 */

public class MovieSyncIntentService extends IntentService {

    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MovieSyncTask.syncMovie(this);

    }
}
