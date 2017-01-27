package com.example.rafal.movieapp.sync.task.serialTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.rafal.movieapp.data.model.Serial;
import com.example.rafal.movieapp.sync.TaskUtility;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static com.example.rafal.movieapp.BuildConfig.MOVIE_API_KEY;

/**
 * Created by rafal on 06.01.2017.
 */

public class FetchInternetSerialIdTask extends AsyncTask<Void, Void, Serial> {


    private final Context mContext;
    private final int _id;

    public FetchInternetSerialIdTask(Context context, int i) {
        mContext = context;
        _id = i;
    }

    @Override
    protected Serial doInBackground(Void... params) {
        try {
            URL url = new URL("http://api.themoviedb.org/3/tv/" + _id + "?api_key=" + MOVIE_API_KEY);

            return TaskUtility.getJSONSerial(TaskUtility.getResponse(url),mContext);

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
        }

        return null;

    }
}
