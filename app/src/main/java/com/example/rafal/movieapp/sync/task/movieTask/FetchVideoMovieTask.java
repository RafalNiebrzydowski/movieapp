package com.example.rafal.movieapp.sync.task.movieTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.rafal.movieapp.utility.Utility;
import com.example.rafal.movieapp.sync.TaskUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.rafal.movieapp.BuildConfig.MOVIE_API_KEY;

/**
 * Created by rafal on 05.01.2017.
 */

public class FetchVideoMovieTask extends AsyncTask<Integer, Void, ArrayList> {


    private final Context mContext;

    public FetchVideoMovieTask(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<String> doInBackground(Integer... voids) {

        try {
            URL url = new URL("http://api.themoviedb.org/3/movie/"+voids[0]+"/videos?api_key="+MOVIE_API_KEY);

            JSONObject object = new JSONObject(TaskUtility.getResponse(url));
            JSONArray array = object.getJSONArray("results");
            ArrayList<String> keys = new ArrayList<String>();
            for(int i=0;i<array.length();i++){
                JSONObject film = array.getJSONObject(i);
                keys.add(film.getString("key"));
            }
            return keys;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            if(Utility.isOnline(mContext))
            doInBackground(voids);
        }
        return null;
    }

}

