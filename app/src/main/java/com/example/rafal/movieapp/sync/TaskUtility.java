package com.example.rafal.movieapp.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.data.model.Episode;
import com.example.rafal.movieapp.data.model.Movie;
import com.example.rafal.movieapp.data.model.Season;
import com.example.rafal.movieapp.data.model.Serial;
import com.example.rafal.movieapp.sync.task.FetchImageTask;
import com.example.rafal.movieapp.sync.task.movieTask.FetchDurationTimeMovieTask;
import com.example.rafal.movieapp.sync.task.movieTask.FetchVideoMovieTask;
import com.example.rafal.movieapp.sync.task.serialTask.FetchSeasonEpisodeTask;
import com.example.rafal.movieapp.sync.task.serialTask.FetchSeasonTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * Created by rafal on 26.01.2017.
 */

public class TaskUtility {

    public static String getResponse(URL url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();
        if (responseCode >= 400 && responseCode <= 499) {
            throw new Exception("Bad authentication status: " + responseCode);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static Movie getJSONMovie(String response, Context context) throws JSONException, ExecutionException, InterruptedException {
        JSONObject film = new JSONObject(response);

        FetchDurationTimeMovieTask fetchDurationTimeMovieTask = new FetchDurationTimeMovieTask(context);
        String runtime = fetchDurationTimeMovieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, film.getInt("id")).get();


        String image = "http://image.tmdb.org/t/p/w342" + film.getString("poster_path");
        if (runtime == null) {
            runtime = "0";
        }
        FetchVideoMovieTask fetchVideoMovieTask = new FetchVideoMovieTask(context);
        ArrayList<String> keys = fetchVideoMovieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, film.getInt("id")).get();


        return new Movie(film.getInt("id"), film.getString("title"), film.getString("overview"), film.getString("release_date"), film.getInt("vote_count"), film.getDouble("vote_average"), 0, image, Integer.parseInt(runtime), keys);

    }

    public static Serial getJSONSerial(String response, Context context) throws JSONException, ExecutionException, InterruptedException {
        JSONObject film = new JSONObject(response);


        String image = "http://image.tmdb.org/t/p/w342" + film.getString("poster_path");
        int numberSeason = film.getInt("number_of_seasons");
        List<Season> seasons = new ArrayList<Season>();
        for (int i = 1; i <= numberSeason; i++) {
            FetchSeasonEpisodeTask fetchSeasonEpisodeTask = new FetchSeasonEpisodeTask(context, i);
            seasons.add(new Season(i, fetchSeasonEpisodeTask.execute(film.getInt("id")).get()));
        }

        return new Serial(film.getInt("id"), film.getString("name"), film.getString("overview"), film.getString("first_air_date"), film.getInt("vote_count"), film.getDouble("vote_average"), 0, image, seasons);
    }

    public static List<Movie> getJSONMovieInternet(String response, Context context) throws JSONException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("results");

        List<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < results.length(); i++) {

            JSONObject film = results.getJSONObject(i);
            FetchDurationTimeMovieTask fetchDurationTimeMovieTask = new FetchDurationTimeMovieTask(context);
            String runtime = fetchDurationTimeMovieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, film.getInt("id")).get();
            String image = "http://image.tmdb.org/t/p/w342" + film.getString("poster_path");

            if (runtime == null) {
                runtime = "0";
            }
            movies.add(new Movie(film.getInt("id"), film.getString("title"), image));

        }
        return movies;

    }

    public static List<Serial> getJSONSerial(String response) throws JSONException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("results");

        List<Serial> movies = new ArrayList<Serial>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject film = results.getJSONObject(i);
            String image = "http://image.tmdb.org/t/p/w342" + film.getString("poster_path");
            movies.add(new Serial(film.getInt("id"), film.getString("name"), image));
        }
        return movies;

    }

    public static List<Season> getJSONSeason(String response, Context context) throws JSONException, ExecutionException, InterruptedException {
        JSONObject film = new JSONObject(response);
        int numberSeason = film.getInt("number_of_seasons");
        List<Season> seasons = new ArrayList<Season>();
        for (int i = 1; i <= numberSeason; i++) {
            FetchSeasonEpisodeTask fetchSeasonEpisodeTask = new FetchSeasonEpisodeTask(context, i);
            seasons.add(new Season(i, fetchSeasonEpisodeTask.execute(film.getInt("id")).get()));
        }

        return seasons;

    }

    public static List<Episode> getJSONEpisode(String response) throws JSONException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("episodes");
        List<Episode> episodes = new ArrayList<Episode>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject episode = results.getJSONObject(i);
            episodes.add(new Episode(episode.getString("name"), episode.getString("air_date")));
        }

        return episodes;

    }

    public static void getJSONMovie(String response, Context context, String path) throws JSONException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("results");
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject film = results.getJSONObject(i);
            ContentValues values = new ContentValues();

            values.put(MovieContract.Movie.COLUMN_TITLE, film.getString("title"));
            values.put(MovieContract.Movie.COLUMN_ID, film.getInt("id"));
            values.put(MovieContract.Movie.COLUMN_OVERVIEW, film.getString("overview"));
            if (path.equals("popular")) {
                values.put(MovieContract.Movie.COLUMN_POPULAR, 1);
                values.put(MovieContract.Movie.COLUMN_TOPRATED, 0);
            } else {
                values.put(MovieContract.Movie.COLUMN_TOPRATED, 1);
                values.put(MovieContract.Movie.COLUMN_POPULAR, 0);
            }
            FetchDurationTimeMovieTask fetchDurationTimeMovieTask = new FetchDurationTimeMovieTask(context);
            String runtime = fetchDurationTimeMovieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, film.getInt("id")).get();
            if (runtime != null) {
                values.put(MovieContract.Movie.COLUMN_DURATION, Integer.parseInt(runtime));
            } else {
                values.put(MovieContract.Movie.COLUMN_DURATION, 0);
            }

            FetchImageTask fetchImageTask = new FetchImageTask(context);
            byte[] image = null;
            if (!film.getString("poster_path").equals("null"))
                image = fetchImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://image.tmdb.org/t/p/w342" + film.getString("poster_path")).get();
           /* else {
                Drawable d = context.getResources().getDrawable(R.drawable.blank);
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                image = stream.toByteArray();

            }*/

            values.put(MovieContract.Movie.COLUMN_IMAGE, image);
            values.put(MovieContract.Movie.COLUMN_DATERELEASE, film.getString("release_date"));
            values.put(MovieContract.Movie.COLUMN_FAVORITE, 0);
            values.put(MovieContract.Movie.COLUMN_VOTECOUNT, film.getInt("vote_count"));
            values.put(MovieContract.Movie.COLUMN_VOTEAVERAGE, film.getDouble("vote_average"));

            Cursor cursor = context.getContentResolver().query(MovieContract.Movie.buildMovieWithID(film.getInt("id")), null, null, null, null);
            if (!cursor.moveToFirst()) {
                context.getContentResolver().insert(MovieContract.Movie.CONTENT_URI, values);
                ids.add(film.getInt("id"));
            } else {

                if (path.equals("popular") && cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_POPULAR)) == 0) {
                    ContentValues values1 = new ContentValues();
                    values1.put(MovieContract.Movie.COLUMN_POPULAR, 1);
                    context.getContentResolver().update(MovieContract.Movie.CONTENT_URI, values1, MovieContract.Movie.COLUMN_ID + " =?", new String[]{String.valueOf(film.getInt("id"))});
                }
                if (path.equals("top_rated") && cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_TOPRATED)) == 0) {
                    ContentValues values1 = new ContentValues();
                    values1.put(MovieContract.Movie.COLUMN_TOPRATED, 1);
                    context.getContentResolver().update(MovieContract.Movie.CONTENT_URI, values1, MovieContract.Movie.COLUMN_ID + " =?", new String[]{String.valueOf(film.getInt("id"))});
                }
                if (path.equals("popular") && cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_POPULAR)) == 1) {
                    if (film.getString("adult") == "true") {

                        context.getContentResolver().update(MovieContract.Movie.CONTENT_URI, values, MovieContract.Movie.COLUMN_ID + " =?", new String[]{String.valueOf(film.getInt("id"))});
                    }
                }
                if (path.equals("top_rated") && cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_TOPRATED)) == 1) {
                    if (film.getString("adult") == "true") {

                        context.getContentResolver().update(MovieContract.Movie.CONTENT_URI, values, MovieContract.Movie.COLUMN_ID + " =?", new String[]{String.valueOf(film.getInt("id"))});
                    }
                }
            }
        }

        for (Integer id : ids) {
            FetchVideoMovieTask fetchVideoMovieTask = new FetchVideoMovieTask(context);
            ArrayList<String> keys = fetchVideoMovieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id).get();
            if (keys != null) {
                Vector<ContentValues> cVVectorVideo = new Vector<ContentValues>(keys.size());
                for (String s : keys) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.VideoMovie.COLUMN_ID, id);
                    contentValues.put(MovieContract.VideoMovie.COLUMN_KEY, s);
                    cVVectorVideo.add(contentValues);
                }
                if (cVVectorVideo.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVectorVideo.size()];
                    cVVectorVideo.toArray(cvArray);
                    context.getContentResolver().bulkInsert(MovieContract.VideoMovie.CONTENT_URI, cvArray);
                }
            }
        }
        context.getContentResolver().notifyChange(MovieContract.Movie.CONTENT_URI, null);
    }

    public static void getJSONSerial(String response, Context context, String path) throws JSONException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject film = results.getJSONObject(i);
            ContentValues values = new ContentValues();

            values.put(MovieContract.Serial.COLUMN_TITLE, film.getString("name"));
            values.put(MovieContract.Serial.COLUMN_ID, film.getInt("id"));
            values.put(MovieContract.Serial.COLUMN_OVERVIEW, film.getString("overview"));
            if (path.equals("popular")) {
                values.put(MovieContract.Serial.COLUMN_POPULAR, 1);
            } else {
                values.put(MovieContract.Serial.COLUMN_TOPRATED, 1);
            }

            FetchImageTask fetchImageTask = new FetchImageTask(context);
            byte[] image = null;
            if (!film.getString("poster_path").equals("null"))
                image = fetchImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://image.tmdb.org/t/p/w342" + film.getString("poster_path")).get();
       /* else {
            Drawable d = mContext.getResources().getDrawable(R.drawable.blank);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            image = stream.toByteArray();

        }*/
            values.put(MovieContract.Serial.COLUMN_IMAGE, image);
            values.put(MovieContract.Serial.COLUMN_DATERELEASE, film.getString("first_air_date"));
            values.put(MovieContract.Serial.COLUMN_FAVORITE, 0);
            values.put(MovieContract.Serial.COLUMN_VOTECOUNT, film.getInt("vote_count"));
            values.put(MovieContract.Serial.COLUMN_VOTEAVERAGE, film.getDouble("vote_average"));
            Cursor cursor = context.getContentResolver().query(MovieContract.Serial.buildSerialWithID(film.getInt("id")), null, null, null, null);
            if (!cursor.moveToFirst())
                context.getContentResolver().insert(MovieContract.Serial.CONTENT_URI, values);
            else {
                if (path.equals("popular") && cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_POPULAR)) == 0) {
                    ContentValues values1 = new ContentValues();
                    values1.put(MovieContract.Serial.COLUMN_POPULAR, 1);
                    context.getContentResolver().update(MovieContract.Serial.CONTENT_URI, values1, MovieContract.Serial.COLUMN_ID + " =?", new String[]{String.valueOf(film.getInt("id"))});
                }
                if (path.equals("top_rated") && cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_TOPRATED)) == 0) {
                    ContentValues values1 = new ContentValues();
                    values1.put(MovieContract.Serial.COLUMN_TOPRATED, 1);
                    context.getContentResolver().update(MovieContract.Serial.CONTENT_URI, values1, MovieContract.Serial.COLUMN_ID + " =?", new String[]{String.valueOf(film.getInt("id"))});
                }

            }
            FetchSeasonTask fetchSerialIdTask = new FetchSeasonTask(context, film.getInt("id"));
            List<Season> serial = fetchSerialIdTask.loadInBackground();

            ArrayList<Integer> ids = new ArrayList<>();
            if (serial != null) {
                Cursor cursorSeason = context.getContentResolver().query(MovieContract.Season.buildSeasonWithID(film.getInt("id")), null, null, null, null);
                if (!cursorSeason.moveToFirst()) {
                    for (int a = 0; a < serial.size(); a++) {
                        ContentValues values1 = new ContentValues();
                        values1.put(MovieContract.Season.COLUMN_ID, film.getInt("id"));
                        values1.put(MovieContract.Season.COLUMN_NUMBER, a);
                        Uri uri = context.getContentResolver().insert(MovieContract.Season.CONTENT_URI, values1);

                        ids.add(Integer.parseInt(Long.toString(MovieContract.Season.getIdFromUri(uri))));


                    }
                    Vector<ContentValues> cVVector = new Vector<ContentValues>(ids.size());
                    for (int a = 0; a < ids.size(); a++) {
                        if (serial.get(a).getEpisodes() != null)
                            for (Episode e : serial.get(a).getEpisodes()) {
                                ContentValues values2 = new ContentValues();
                                values2.put(MovieContract.Epsiode.COLUMN_ID, ids.get(a));
                                values2.put(MovieContract.Epsiode.COLUMN_TITLE, e.getTitle());
                                values2.put(MovieContract.Epsiode.COLUMN_DATERELEASE, e.getDate());
                                cVVector.add(values2);

                            }
                    }
                    if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);
                        context.getContentResolver().bulkInsert(MovieContract.Epsiode.CONTENT_URI, cvArray);
                    }

                }
            }
        }
        context.getContentResolver().notifyChange(MovieContract.Serial.CONTENT_URI, null);
    }
}
