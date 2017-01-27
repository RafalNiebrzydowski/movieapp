package com.example.rafal.movieapp.sync.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.test.espresso.core.deps.guava.io.ByteStreams;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by rafal on 05.01.2017.
 */
public class FetchImageTask extends AsyncTask <String, Void, byte[]> {
    private final Context mContext;

    public FetchImageTask(Context context){
        mContext = context;
    }
    @Override
    protected byte[] doInBackground(String... voids) {
        InputStream is=null;
        byte[] image = null;
        try {

            is = (InputStream) new URL(voids[0]).getContent();
            image = ByteStreams.toByteArray(is);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return image;

    }
}
