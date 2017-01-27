package com.example.rafal.movieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rafal.movieapp.adapter.episodeAdapter.EpisodeAdapter;
import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.data.model.Season;
import com.example.rafal.movieapp.data.model.Serial;
import com.example.rafal.movieapp.sync.task.serialTask.FetchSerialIdTask;
import com.example.rafal.movieapp.utility.PreferenceUtils;
import com.example.rafal.movieapp.utility.Utility;

import java.util.List;

public class DetailSerialInternetActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Serial> {
    private ShareActionProvider mShareActionProvider;
    private String mMovie;
    private long _id;
    Toolbar toolbar;
    private Serial serial;
    FloatingActionButton fab;
    int isFavorite = 0;
    private static final int ID_DETAILMOVIE_LOADER = 1;
    private static String mTheme;
    private static final String MOVIE_SHARE_HASHTAG = " #MoviesApp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mTheme = sharedPreferences.getString(getString(R.string.pref_style_key), getString(R.string.pref_style_default));
        PreferenceUtils.changeStyle(sharedPreferences, this, mTheme);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        _id = getIntent().getLongExtra(Intent.EXTRA_TEXT, -1);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFavorite == 1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 0);
                    String selection = MovieContract.Serial.COLUMN_ID + "= ? AND " + MovieContract.Serial.COLUMN_POPULAR + " = 0 " + " AND " + MovieContract.Serial.COLUMN_TOPRATED + " = 0";
                    String[] selectionArgs = {Long.toString(_id)};
                    String selection2 = MovieContract.Serial.COLUMN_ID + "= ?";
                    Utility.removeFromFavoriteFromInternet(view.getContext(), _id, view, contentValues, selection, selection2, MovieContract.Serial.CONTENT_URI);
                    isFavorite = 0;
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 1);
                    String selection = MovieContract.Serial.COLUMN_ID + "= ?";
                    String[] selectionArgs = {Long.toString(_id)};
                    Utility.addToFavoriteFromInternet(view.getContext(), _id, view, contentValues,selection, MovieContract.Serial.CONTENT_URI,false);
//                    Cursor cursor = getContentResolver().query(MovieContract.Serial.buildSerialWithID(_id), null, null, null, null);
//                    if (cursor.moveToFirst()) {
//                        if (cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_FAVORITE)) == 0) {
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 1);
//                            String selection = MovieContract.Serial.COLUMN_ID + "= ?";
//                            String[] selectionArgs = {Long.toString(_id)};
//                            getContentResolver().update(MovieContract.Serial.CONTENT_URI, contentValues, selection, selectionArgs);
//                        }
//                    } else {
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put(MovieContract.Serial.COLUMN_ID, _id);
//                        contentValues.put(MovieContract.Serial.COLUMN_TITLE, serial.getTitle());
//                        contentValues.put(MovieContract.Serial.COLUMN_OVERVIEW, serial.getOverview());
//                        contentValues.put(MovieContract.Serial.COLUMN_DATERELEASE, serial.getDateRelease());
//                        contentValues.put(MovieContract.Serial.COLUMN_VOTECOUNT, serial.getVoteCount());
//                        contentValues.put(MovieContract.Serial.COLUMN_VOTEAVERAGE, serial.getVoteAverage());
//                        contentValues.put(MovieContract.Serial.COLUMN_IMAGE, serial.getImage());
//                        contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 1);
//                        contentValues.put(MovieContract.Serial.COLUMN_POPULAR, 0);
//                        contentValues.put(MovieContract.Serial.COLUMN_TOPRATED, 0);
//                        Uri uri = getContentResolver().insert(MovieContract.Serial.CONTENT_URI, contentValues);
//                        ArrayList<Integer> ids = new ArrayList<>();
//                        if (serial != null)
//                            for (int restartLoader = 0; restartLoader < serial.getSeasons().size(); restartLoader++) {
//                                ContentValues values1 = new ContentValues();
//                                values1.put(MovieContract.Season.COLUMN_ID, _id);
//                                values1.put(MovieContract.Season.COLUMN_NUMBER, restartLoader);
//                                Uri uri2 = getContentResolver().insert(MovieContract.Season.CONTENT_URI, values1);
//
//                                ids.add(Integer.parseInt(Long.toString(MovieContract.Season.getIdFromUri(uri2))));
//
//
//                            }
//                        for (int restartLoader = 0; restartLoader < ids.size(); restartLoader++) {
//                            if (serial.getSeasons().get(restartLoader).getEpisodes() != null)
//                                for (Episode e : serial.getSeasons().get(restartLoader).getEpisodes()) {
//                                    ContentValues values2 = new ContentValues();
//                                    values2.put(MovieContract.Epsiode.COLUMN_ID, ids.get(restartLoader));
//                                    values2.put(MovieContract.Epsiode.COLUMN_TITLE, e.getTitle());
//                                    values2.put(MovieContract.Epsiode.COLUMN_DATERELEASE, e.getDate());
//                                    getContentResolver().insert(MovieContract.Epsiode.CONTENT_URI, values2);
//                                }
//                        }
//                    }
//
//
//                    Snackbar snackbar;
//                    snackbar = Snackbar.make(view, "Added movie to favorite", Snackbar.LENGTH_LONG).setAction("Favorite", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Bundle b = new Bundle();
//                            b.putInt("pos", 1);
//                            startActivity(new Intent(v.getContext(), TabActivity.class).putExtras(b));
//                        }
//                    });
//                    View snackBarView = snackbar.getView();
//                    TypedValue typedValue = new TypedValue();
//                    Resources.Theme theme = getTheme();
//                    theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
//                    int color = typedValue.data;
//                    snackBarView.setBackgroundColor(color);
//                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
//                    textView.setTextColor(getResources().getColor(android.R.color.black));
//                    snackbar.show();

                    isFavorite = 1;
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                }


            }
        });

        getSupportLoaderManager().initLoader(ID_DETAILMOVIE_LOADER, null, this).forceLoad();
    }

    @Override
    public Loader<Serial> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_DETAILMOVIE_LOADER:

                return new FetchSerialIdTask(this, Integer.parseInt(String.valueOf(_id)));

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = PreferenceUtils.changeStyle(sharedPreferences, this, mTheme);
        if (theme != null) {
            mTheme = theme;
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mMovie != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie + MOVIE_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onLoadFinished(Loader<Serial> loader, Serial movie) {
        boolean cursorHasValidData = false;
        if (movie != null) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }
        this.serial = movie;
        TextView durationTextView = (TextView) this.findViewById(R.id.duration_textview);
        durationTextView.setVisibility(View.GONE);
        TextView overviewTextView = (TextView) this.findViewById(R.id.overview_textview);
        ImageView posterImageView = (ImageView) this.findViewById(R.id.poster_imageview);
        TextView dateTextView = (TextView) this.findViewById(R.id.year_textview);
        TextView voteCountTextView = (TextView) this.findViewById(R.id.votecount_textview);
        TextView voteAverageTextView = (TextView) this.findViewById(R.id.voteaverage_textview);
        overviewTextView.setText(movie.getOverview());
        dateTextView.setText(movie.getDateRelease());
        voteAverageTextView.setText(Utility.voteAvarage(Double.toString(movie.getVoteAverage())));
        voteCountTextView.setText(Utility.voteCount(Integer.toString(movie.getVoteCount())));

        Glide.with(this)
                .load(movie.getImage())
                .placeholder(R.drawable.blank)
                .into(posterImageView);
        mMovie = movie.getTitle() + " " + dateTextView.getText();
        Cursor cursor = getContentResolver().query(MovieContract.Serial.buildSerialWithID(_id), null, null, null, null);
        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_FAVORITE)) == 0) {
                isFavorite = 0;
            } else
                isFavorite = 1;
        } else {
            isFavorite = 0;
        }
        mMovie = movie.getTitle() + " " + movie.getDateRelease();
        if (isFavorite == 1) {
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else
            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);


        LinearLayout layout = (LinearLayout) findViewById(R.id.detail_layout);
        ListView trailerList = (ListView) findViewById(R.id.trailer_list);
        trailerList.setVisibility(View.GONE);
        List<Season> seasons = movie.getSeasons();
        for (int i = 0; i < seasons.size(); i++) {
            Button button = Utility.generateButton(this,i);
            layout.addView(button);

            if (seasons.get(i) != null) {
                final ListView listView = new ListView(this);
                EpisodeAdapter episodeAdapter = new EpisodeAdapter(this, seasons.get(i).getEpisodes());
                LinearLayout.LayoutParams paramsListView = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                listView.setAdapter(episodeAdapter);
                listView.setLayoutParams(paramsListView);
                layout.addView(listView);
                int count = seasons.get(i).getEpisodes().size();
                float height = Utility.getPrefferedItemHeight(this);
                listView.getLayoutParams().height = ((count + 1) * (int) height);
                listView.setVisibility(View.GONE);


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listView.getVisibility() == View.VISIBLE) {
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }


        }
        Space view = new Space(this);
        toolbar.setTitle(movie.getTitle());
        layout.addView(view);
        view.getLayoutParams().height = 68;
    }

    @Override
    public void onLoaderReset(Loader<Serial> loader) {

    }
}
