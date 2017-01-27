package com.example.rafal.movieapp.sync.task.serialTask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

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

public class FetchSerialIdTask extends AsyncTaskLoader<Serial> {


    private final Context mContext;
    private final int _id;

    public FetchSerialIdTask(Context context, int i) {
        super(context);
        mContext = context;
        _id = i;
    }

    @Override
    public Serial loadInBackground() {
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
