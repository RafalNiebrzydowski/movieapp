package com.example.rafal.movieapp.sync.task.serialTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.rafal.movieapp.utility.Utility;
import com.example.rafal.movieapp.data.model.Episode;
import com.example.rafal.movieapp.sync.TaskUtility;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.rafal.movieapp.BuildConfig.MOVIE_API_KEY;

/**
 * Created by rafal on 05.01.2017.
 */

public class FetchSeasonEpisodeTask extends AsyncTask<Integer, Void, List<Episode>> {


    private final Context mContext;
    private int season;

    public FetchSeasonEpisodeTask(Context context, int season) {
        mContext = context;
        this.season = season;
    }

    @Override
    protected List<Episode> doInBackground(Integer... voids) {

        try {
            URL url = new URL("https://api.themoviedb.org/3/tv/"+voids[0]+"/season/"+season+"?api_key="+MOVIE_API_KEY);

            return TaskUtility.getJSONEpisode(TaskUtility.getResponse(url));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            if(Utility.isOnline(mContext))
            doInBackground(voids);
        }
        return null;
    }

}

