package com.example.rafal.movieapp.sync.task.movieTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.rafal.movieapp.utility.Utility;
import com.example.rafal.movieapp.sync.TaskUtility;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static com.example.rafal.movieapp.BuildConfig.MOVIE_API_KEY;

/**
 * Created by rafal on 05.01.2017.
 */

public class FetchDurationTimeMovieTask extends AsyncTask<Integer, Void, String> {

    private Context mContext;
    public FetchDurationTimeMovieTask(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Integer... voids) {

        try {
            URL url = new URL("http://api.themoviedb.org/3/movie/"+voids[0]+"?api_key="+MOVIE_API_KEY);

            JSONObject object = new JSONObject(TaskUtility.getResponse(url));
            return object.getString("runtime");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
            if(Utility.isOnline(mContext))
            doInBackground(voids);
        }
        return null;
    }


}
