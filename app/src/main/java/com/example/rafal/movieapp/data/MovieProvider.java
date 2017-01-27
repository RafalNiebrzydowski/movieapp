package com.example.rafal.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by rafal on 13.01.2017.
 */

public class MovieProvider extends ContentProvider {

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieDbHelper movieDbHelper;
    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;
    private static final int VIDEOMOVIE = 300;
    private static final int VIDEOMOVIE_ID = 301;
    private static final int SERIAL = 400;
    private static final int SERIAL_ID = 401;
    private static final int SEASON = 500;
    private static final int SEASON_ID = 501;
    private static final int EPISODE = 600;
    private static final int EPISODE_ID = 601;

    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIE);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_VIDEOMOVIE, VIDEOMOVIE);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_VIDEOMOVIE + "/#", VIDEOMOVIE_ID);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_SERIAL, SERIAL);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_SERIAL + "/#", SERIAL_ID);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_SEASON, SEASON);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_SEASON + "/#", SEASON_ID);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_EPISODE, EPISODE);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_EPISODE + "/#", EPISODE_ID);

    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case MOVIE: {
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.Movie.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case MOVIE_ID: {
                selection = MovieContract.Movie.COLUMN_ID + " = ?";
                selectionArgs = new String[]{Long.toString(MovieContract.Movie.getIdFromUri(uri))};
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.Movie.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case VIDEOMOVIE: {
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.VideoMovie.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case VIDEOMOVIE_ID: {
                selection = MovieContract.VideoMovie.COLUMN_ID + " = ?";
                selectionArgs = new String[]{Long.toString(MovieContract.VideoMovie.getIdFromUri(uri))};
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.VideoMovie.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case SERIAL: {
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.Serial.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case SERIAL_ID: {
                selection = MovieContract.Serial.TABLE_NAME + "." + MovieContract.Serial.COLUMN_ID + " = ?";
                selectionArgs = new String[]{Long.toString(MovieContract.Serial.getIdFromUri(uri))};
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.Serial.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case SEASON_ID: {

                selection = MovieContract.Season.TABLE_NAME + "." + MovieContract.Season.COLUMN_ID + " = ?";
                selectionArgs = new String[]{Long.toString(MovieContract.Serial.getIdFromUri(uri))};
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.Season.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case EPISODE_ID: {
                selection = MovieContract.Epsiode.TABLE_NAME + "." + MovieContract.Epsiode.COLUMN_ID + " = ?";
                selectionArgs = new String[]{Long.toString(MovieContract.Epsiode.getIdFromUri(uri))};
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.Epsiode.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return MovieContract.Movie.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.Movie.CONTENT_TYPE_ITEM;
            case VIDEOMOVIE:
                return MovieContract.VideoMovie.CONTENT_TYPE;
            case VIDEOMOVIE_ID:
                return MovieContract.VideoMovie.CONTENT_TYPE_ITEM;
            case SERIAL:
                return MovieContract.Serial.CONTENT_TYPE;
            case SERIAL_ID:
                return MovieContract.Serial.CONTENT_TYPE_ITEM;
            case SEASON:
                return MovieContract.Season.CONTENT_TYPE;
            case SEASON_ID:
                return MovieContract.Season.CONTENT_TYPE_ITEM;
            case EPISODE:
                return MovieContract.Epsiode.CONTENT_TYPE;
            case EPISODE_ID:
                return MovieContract.Epsiode.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.Movie.TABLE_NAME, null, contentValues);
                if (_id < 0)
                    returnUri = MovieContract.Movie.buildMovieWithID(_id);
                break;
            }
            case VIDEOMOVIE: {
                long _id = db.insert(MovieContract.VideoMovie.TABLE_NAME, null, contentValues);
                if (_id < 0)
                    returnUri = MovieContract.VideoMovie.buildVideoMovieWithID(_id);
                break;
            }
            case SERIAL: {
                long _id = db.insert(MovieContract.Serial.TABLE_NAME, null, contentValues);
                if (_id < 0)
                    returnUri = MovieContract.Serial.buildSerialWithID(_id);
                break;
            }
            case SEASON: {
                long _id = db.insert(MovieContract.Season.TABLE_NAME, null, contentValues);
                //if (_id < 0)
                returnUri = MovieContract.Season.buildSeasonWithID(_id);
                break;
            }
            case EPISODE: {
                long _id = db.insert(MovieContract.Epsiode.TABLE_NAME, null, contentValues);
                if (_id < 0)
                    returnUri = MovieContract.Epsiode.buildEpisodeWithID(_id);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknow Uri" + uri);

        }
        if (returnUri != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.Movie.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEOMOVIE:
                rowsDeleted = db.delete(
                        MovieContract.VideoMovie.TABLE_NAME, selection, selectionArgs);
                break;
            case SERIAL:
                rowsDeleted = db.delete(
                        MovieContract.Serial.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.Movie.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case SERIAL:
                rowsUpdated = db.update(MovieContract.Serial.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] contentValues) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        long _id = db.insert(MovieContract.Movie.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case VIDEOMOVIE:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        long _id = db.insert(MovieContract.VideoMovie.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case SERIAL: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        long _id = db.insert(MovieContract.Serial.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case SEASON: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        long _id = db.insert(MovieContract.Season.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case EPISODE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        long _id = db.insert(MovieContract.Epsiode.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, contentValues);
        }
    }
}