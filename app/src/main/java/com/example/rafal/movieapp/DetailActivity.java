package com.example.rafal.movieapp;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rafal.movieapp.adapter.trailerAdapter.TrailerCursorAdapter;
import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.utility.PreferenceUtils;
import com.example.rafal.movieapp.utility.Utility;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ShareActionProvider mShareActionProvider;
    private String mMovie;
    private Uri mUri;
    Toolbar toolbar;
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
        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFavorite == 1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 0);
                    String selection = MovieContract.Movie.COLUMN_ID + "= ?";
                    String selectionArgs = Long.toString(MovieContract.Movie.getIdFromUri(mUri));
                    Utility.removeFromFavorite(view.getContext(), selectionArgs, view, contentValues, selection, MovieContract.Movie.CONTENT_URI);
                    isFavorite = 0;
                } else {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 1);
                    String selection = MovieContract.Movie.COLUMN_ID + "= ?";
                    String selectionArgs = Long.toString(MovieContract.Movie.getIdFromUri(mUri));
                    Utility.addToFavorite(view.getContext(), selectionArgs, view, contentValues, selection, MovieContract.Movie.CONTENT_URI);
                    isFavorite = 1;

                }

            }
        });
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        getSupportLoaderManager().initLoader(ID_DETAILMOVIE_LOADER, null, this);
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
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_DETAILMOVIE_LOADER:
                Uri movieQueryUri = mUri;

                return new CursorLoader(this,
                        movieQueryUri,
                        null,
                        null,
                        null,
                        null);

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        boolean cursorHasValidData = false;
        if (cursor != null && cursor.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }
        TextView overviewTextView = (TextView) this.findViewById(R.id.overview_textview);
        ImageView posterImageView = (ImageView) this.findViewById(R.id.poster_imageview);
        TextView dateTextView = (TextView) this.findViewById(R.id.year_textview);
        TextView durationTextView = (TextView) this.findViewById(R.id.duration_textview);
        TextView voteCountTextView = (TextView) this.findViewById(R.id.votecount_textview);
        TextView voteAverageTextView = (TextView) this.findViewById(R.id.voteaverage_textview);
        toolbar.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_TITLE)));
        overviewTextView.setText(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_OVERVIEW)));
        dateTextView.setText(Utility.getYearFromDate(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_DATERELEASE))));
        durationTextView.setText(Utility.addMinsToDuration(Integer.toString(cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_DURATION)))));
        voteAverageTextView.setText(Utility.voteAvarage(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_VOTEAVERAGE))));
        voteCountTextView.setText(Utility.voteCount(Integer.toString(cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_VOTECOUNT)))));

        Glide.with(this)
                .load(cursor.getBlob(cursor.getColumnIndex(MovieContract.Movie.COLUMN_IMAGE)))
                .asBitmap()
                .into(posterImageView);

        isFavorite = cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_FAVORITE));
        mMovie = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_TITLE)) + " " + dateTextView.getText();
        if (isFavorite == 1) {
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else
            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        ListView listView = (ListView) findViewById(R.id.trailer_list);

        float height = Utility.getPrefferedItemHeight(this);
        Cursor videoMovie = getContentResolver().query(MovieContract.VideoMovie.buildVideoMovieWithID(MovieContract.VideoMovie.getIdFromUri(mUri)), null, null, null, null);
        listView.getLayoutParams().height = (videoMovie.getCount() + 1) * (int) height;

        TrailerCursorAdapter trailerCursorAdapter = new TrailerCursorAdapter(this, videoMovie, 0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                if (cursor != null) {
                    String key = (cursor.getString(cursor.getColumnIndex(MovieContract.VideoMovie.COLUMN_KEY)));

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
                }
            }
        });
        listView.setAdapter(trailerCursorAdapter);
        toolbar.setBackground(posterImageView.getDrawable());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
