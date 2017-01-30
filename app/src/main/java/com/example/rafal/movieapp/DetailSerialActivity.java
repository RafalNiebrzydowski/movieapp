package com.example.rafal.movieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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
import com.example.rafal.movieapp.adapter.episodeAdapter.EpisodeCursorAdapter;
import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.utility.PreferenceUtils;
import com.example.rafal.movieapp.utility.Utility;

public class DetailSerialActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ShareActionProvider mShareActionProvider;
    CollapsingToolbarLayout collapsingToolbarLayout;
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
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
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
                    contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 0);
                    String selection = MovieContract.Serial.COLUMN_ID + "= ?";
                    String selectionArgs = Long.toString(MovieContract.Serial.getIdFromUri(mUri));
                    Utility.removeFromFavorite(view.getContext(), selectionArgs, view, contentValues, selection, MovieContract.Serial.CONTENT_URI);
                    isFavorite = 0;
                } else {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.Serial.COLUMN_FAVORITE, 1);
                    String selection = MovieContract.Serial.COLUMN_ID + "= ?";
                    String selectionArgs = Long.toString(MovieContract.Serial.getIdFromUri(mUri));
                    Utility.addToFavorite(view.getContext(), selectionArgs, view, contentValues, selection, MovieContract.Serial.CONTENT_URI);
                    isFavorite = 1;

                }

            }
        });
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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        boolean cursorHasValidData = false;
        if (cursor != null && cursor.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }
        TextView txtLastName = (TextView) this.findViewById(R.id.overview_textview);
        ImageView imageView = (ImageView) this.findViewById(R.id.poster_imageview);
        TextView txtReleaseDate = (TextView) this.findViewById(R.id.year_textview);
        TextView txtDuration = (TextView) this.findViewById(R.id.duration_textview);
        txtDuration.setVisibility(View.GONE);
        TextView txtVoteCount = (TextView) this.findViewById(R.id.votecount_textview);
        TextView txtVoteAverage = (TextView) this.findViewById(R.id.voteaverage_textview);
        collapsingToolbarLayout.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.Serial.COLUMN_TITLE)));
        txtLastName.setText(cursor.getString(cursor.getColumnIndex(MovieContract.Serial.COLUMN_OVERVIEW)));
        txtReleaseDate.setText(Utility.getYearFromDate(cursor.getString(cursor.getColumnIndex(MovieContract.Serial.COLUMN_DATERELEASE))));
        txtVoteAverage.setText(Utility.voteAvarage(cursor.getString(cursor.getColumnIndex(MovieContract.Serial.COLUMN_VOTEAVERAGE))));
        txtVoteCount.setText(Utility.voteCount(Integer.toString(cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_VOTECOUNT)))));

        Glide.with(this)
                .load(cursor.getBlob(cursor.getColumnIndex(MovieContract.Serial.COLUMN_IMAGE)))
                .asBitmap()
                .into(imageView);
        isFavorite = cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_FAVORITE));
        mMovie = cursor.getString(cursor.getColumnIndex(MovieContract.Serial.COLUMN_TITLE)) + " " + txtReleaseDate.getText();
        if (isFavorite == 1) {
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else
            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);


        ListView listViewTrailer = (ListView) this.findViewById(R.id.trailer_list);
        listViewTrailer.setVisibility(View.GONE);

        LinearLayout layout = (LinearLayout) findViewById(R.id.detail_layout);

        Cursor cursorSeason = getContentResolver().query(MovieContract.Season.buildSeasonWithID(cursor.getInt(cursor.getColumnIndex(MovieContract.Serial.COLUMN_ID))), null, null, null, null);
        while (cursorSeason.moveToNext()) {
            Button button = Utility.generateButton(this, (cursorSeason.getPosition()));
            layout.addView(button);

            Cursor cursorEpisode = getContentResolver().query(MovieContract.Epsiode.buildEpisodeWithID(cursorSeason.getInt(cursorSeason.getColumnIndex(MovieContract.Season._ID))), null, null, null, null);
            if (cursorEpisode != null) {
                final ListView listView = new ListView(this);
                EpisodeCursorAdapter episodeAdapter = new EpisodeCursorAdapter(this, cursorEpisode, 0);
                LinearLayout.LayoutParams paramsListView = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                listView.setAdapter(episodeAdapter);
                listView.setLayoutParams(paramsListView);
                layout.addView(listView);
                int count = cursorEpisode.getCount();
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

        layout.addView(view);
        view.getLayoutParams().height = 68;
        ImageView imageToolbar = (ImageView) collapsingToolbarLayout.findViewById(R.id.imageToolbar);
        Glide.with(this)
                .load(cursor.getBlob(cursor.getColumnIndex(MovieContract.Serial.COLUMN_POSTER)))
                .asBitmap()
                .into(imageToolbar);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
