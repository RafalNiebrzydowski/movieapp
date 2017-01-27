package com.example.rafal.movieapp.sync;

import android.content.Context;

import com.example.rafal.movieapp.utility.NotificationUtils;
import com.example.rafal.movieapp.utility.Utility;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static com.example.rafal.movieapp.BuildConfig.MOVIE_API_KEY;

/**
 * Created by rafal on 13.01.2017.
 */

public class MovieSyncTask {

    synchronized public static void syncMovie(Context context) {
        getMovie(context, "popular");
        getMovie(context, "top_rated");
        getSerial(context, "popular");
        getSerial(context, "top_rated");
        NotificationUtils.createNotifiaction(context);
    }

    public static void getMovie(Context context, String path) {
        try {
            URL url = new URL("http://api.themoviedb.org/3/movie/" + path + "?api_key=" + MOVIE_API_KEY);

            TaskUtility.getJSONMovie(TaskUtility.getResponse(url), context, path);

        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            if(Utility.isOnline(context))
            getMovie(context, path);
        }
    }

    public static void getSerial(Context context, String path) {
        try {
            URL url = new URL("http://api.themoviedb.org/3/tv/" + path + "?api_key=" + MOVIE_API_KEY);

            TaskUtility.getJSONSerial(TaskUtility.getResponse(url), context, path);

        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            if(Utility.isOnline(context))
            getSerial(context, path);
        }

    }

}