package com.example.rafal.movieapp.sync.task.serialTask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rafal.movieapp.data.model.Serial;
import com.example.rafal.movieapp.sync.TaskUtility;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.rafal.movieapp.BuildConfig.MOVIE_API_KEY;

/**
 * Created by rafal on 06.01.2017.
 */

public class FetchSerialTask extends AsyncTaskLoader<List<Serial>> {


    private final Context mContext;
    private final int page_number;
    private final String sort;
    private final boolean isSearch;
    private final String query;

    public FetchSerialTask(Context context, int i, String sort, boolean isSearch, String query) {
        super(context);
        mContext = context;
        page_number = i;
        this.sort = sort;
        this.isSearch = isSearch;
        this.query = query;
    }

    @Override
    public List<Serial> loadInBackground() {
        try {
            URL url;
            if (!isSearch)
                url = new URL("http://api.themoviedb.org/3/discover/tv?" + sort + "api_key=" + MOVIE_API_KEY + "&page=" + page_number);
            else
                url = new URL("http://api.themoviedb.org/3/search/tv?" + sort + "api_key=" + MOVIE_API_KEY + "&query=" + query + "&page=" + page_number);

            return TaskUtility.getJSONSerial(TaskUtility.getResponse(url));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
