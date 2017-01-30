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
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
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
import com.example.rafal.movieapp.adapter.trailerAdapter.TrailerAdapter;
import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.data.model.Movie;
import com.example.rafal.movieapp.sync.task.movieTask.FetchMovieIdTask;
import com.example.rafal.movieapp.utility.PreferenceUtils;
import com.example.rafal.movieapp.utility.Utility;

public class DetailInternetActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {
    private ShareActionProvider mShareActionProvider;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private String mMovie;
    private long _id;
    Toolbar toolbar;
    private Movie movie;
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
        _id = getIntent().getLongExtra(Intent.EXTRA_TEXT, -1);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFavorite == 1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 0);
                    String selection = MovieContract.Movie.COLUMN_ID + "= ? AND " + MovieContract.Movie.COLUMN_POPULAR + " = 0 " + " AND " + MovieContract.Movie.COLUMN_TOPRATED + " = 0";
                    String[] selectionArgs = {Long.toString(_id)};
                    String selection2 = MovieContract.Movie.COLUMN_ID + "= ?";
                    Utility.removeFromFavoriteFromInternet(view.getContext(), _id, view, contentValues,selection,selection2, MovieContract.Movie.CONTENT_URI);
                   /* int uri = getContentResolver().delete(MovieContract.Movie.CONTENT_URI, selection, selectionArgs);
                    if (uri == 0) {


                        String selection2 = MovieContract.Movie.COLUMN_ID + "= ?";
                        String[] selectionArgs2 = {Long.toString(_id)};
                        getContentResolver().update(MovieContract.Movie.CONTENT_URI, contentValues, selection2, selectionArgs2);
                    }
                    Snackbar snackbar;
                    snackbar = Snackbar.make(view, "Removed movie from favorite", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(android.R.color.white));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                    snackbar.show();*/
                    isFavorite = 0;
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 1);
                    String selection = MovieContract.Movie.COLUMN_ID + "= ?";
                    String[] selectionArgs = {Long.toString(_id)};
                    Utility.addToFavoriteFromInternet(view.getContext(), _id, view, contentValues, selection, MovieContract.Movie.CONTENT_URI,true);
//                    Cursor cursor = getContentResolver().query(MovieContract.Movie.buildMovieWithID(_id), null, null, null, null);
//                    if (cursor.moveToFirst()) {
//                        if (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_FAVORITE)) == 0) {
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 1);
//                            String selection = MovieContract.Movie.COLUMN_ID + "= ?";
//                            String[] selectionArgs = {Long.toString(_id)};
//                            getContentResolver().update(MovieContract.Movie.CONTENT_URI, contentValues, selection, selectionArgs);
//                        }
//                    } else {
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put(MovieContract.Movie.COLUMN_ID, _id);
//                        contentValues.put(MovieContract.Movie.COLUMN_TITLE, movie.getTitle());
//                        contentValues.put(MovieContract.Movie.COLUMN_OVERVIEW, movie.getOverview());
//                        contentValues.put(MovieContract.Movie.COLUMN_DATERELEASE, movie.getDateRelease());
//                        contentValues.put(MovieContract.Movie.COLUMN_DURATION, movie.getDuration());
//                        contentValues.put(MovieContract.Movie.COLUMN_VOTECOUNT, movie.getVoteCount());
//                        contentValues.put(MovieContract.Movie.COLUMN_VOTEAVERAGE, movie.getVoteAverage());
//                        contentValues.put(MovieContract.Movie.COLUMN_IMAGE, movie.getImage());
//                        contentValues.put(MovieContract.Movie.COLUMN_FAVORITE, 1);
//                        contentValues.put(MovieContract.Movie.COLUMN_POPULAR, 0);
//                        contentValues.put(MovieContract.Movie.COLUMN_TOPRATED, 0);
//                        Uri uri = getContentResolver().insert(MovieContract.Movie.CONTENT_URI, contentValues);
//                        ArrayList<String> keys = movie.getVideo();
//                        if (keys != null) {
//                            Vector<ContentValues> cVVectorVideo = new Vector<ContentValues>(keys.size());
//                            for (String s : keys) {
//                                ContentValues contentValues2 = new ContentValues();
//                                contentValues2.put(MovieContract.VideoMovie.COLUMN_ID, _id);
//                                contentValues2.put(MovieContract.VideoMovie.COLUMN_KEY, s);
//                                cVVectorVideo.add(contentValues2);
//                            }
//                            if (cVVectorVideo.size() > 0) {
//                                ContentValues[] cvArray = new ContentValues[cVVectorVideo.size()];
//                                cVVectorVideo.toArray(cvArray);
//                                getContentResolver().bulkInsert(MovieContract.VideoMovie.CONTENT_URI, cvArray);
//                            }
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


//                Snackbar.make(view, "Added movie to favorite", Snackbar.LENGTH_LONG)
//                        .setAction("Favorite", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                            }
//                        }).show();
            }
        });

        getSupportLoaderManager().initLoader(ID_DETAILMOVIE_LOADER, null, this).forceLoad();
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
    public Loader<Movie> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_DETAILMOVIE_LOADER:

                return new FetchMovieIdTask(this, Integer.parseInt(String.valueOf(_id)));

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
    public void onLoadFinished(Loader<Movie> loader, Movie movie) {
        boolean cursorHasValidData = false;
        if (movie != null) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }
        this.movie = movie;
        collapsingToolbarLayout.setTitle(movie.getTitle());
        TextView overviewTextView = (TextView) this.findViewById(R.id.overview_textview);
        ImageView posterTextView = (ImageView) this.findViewById(R.id.poster_imageview);
        TextView dateTextView = (TextView) this.findViewById(R.id.year_textview);
        TextView durationTextView = (TextView) this.findViewById(R.id.duration_textview);
        TextView voteCountTextView = (TextView) this.findViewById(R.id.votecount_textview);
        TextView voteAverageTextView = (TextView) this.findViewById(R.id.voteaverage_textview);

        if (!movie.getOverview().equals("null"))
            overviewTextView.setText(movie.getOverview());
        dateTextView.setText(movie.getDateRelease());
        durationTextView.setText(Utility.addMinsToDuration(Integer.toString(movie.getDuration())));
        voteAverageTextView.setText(Utility.voteAvarage(Double.toString(movie.getVoteAverage())));
        voteCountTextView.setText(Utility.voteCount(Integer.toString(movie.getVoteCount())));

        Glide.with(this)
                .load(movie.getImage())
                .placeholder(R.drawable.blank)
                .into(posterTextView);
        mMovie = movie.getTitle() + " " + dateTextView.getText();
        Cursor cursor = getContentResolver().query(MovieContract.Movie.buildMovieWithID(_id), null, null, null, null);
        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_FAVORITE)) == 0) {
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

        ListView listView = (ListView) this.findViewById(R.id.trailer_list);

        float height = Utility.getPrefferedItemHeight(this);

        listView.getLayoutParams().height = (movie.getVideo().size() + 1) * (int) height;
        if (movie.getVideo().size() > 0) {
            TrailerAdapter trailerAdapter = new TrailerAdapter(this, movie.getVideo());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String cursor = (String) adapterView.getItemAtPosition(i);
                    if (cursor != null) {


                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + cursor)));
                    }
                }
            });
            listView.setAdapter(trailerAdapter);
            ImageView imageToolbar = (ImageView) collapsingToolbarLayout.findViewById(R.id.imageToolbar);
            Glide.with(this)
                    .load(movie.getPoster())
                    .placeholder(R.drawable.blank)
                    .into(imageToolbar);
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }
}
