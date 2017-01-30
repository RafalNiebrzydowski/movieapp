package com.example.rafal.movieapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rafal on 13.01.2017.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.rafal.movieapp";
    public static final Uri CONTENT_BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "Movie";
    public static final String PATH_VIDEOMOVIE = "FavoriteMovie";
    public static final String PATH_SERIAL = "Serial";
    public static final String PATH_SEASON = "Season";
    public static final String PATH_EPISODE = "Episode";

    public final static class Movie implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_BASE_URI, PATH_MOVIE);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "Movie";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ID = "id_movie";
        public static final String COLUMN_DATERELEASE = "data_release";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_VOTECOUNT = "vote_count";
        public static final String COLUMN_VOTEAVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "over_view";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_POPULAR = "popular";
        public static final String COLUMN_TOPRATED = "top_rated";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_POSTER = "poster";


        public static Uri buildMovieWithID(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

        public static Long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static final String CREATE_TABLE_MOVIE = "CREATE TABLE " + Movie.TABLE_NAME + " ( " +
                Movie._ID + " INTEGER PRIMARY KEY, "
                + Movie.COLUMN_TITLE + " TEXT NOT NULL, " +
                Movie.COLUMN_ID + " INTEGER NOT NULL UNIQUE, " +
                Movie.COLUMN_DATERELEASE + " TEXT, " +
                Movie.COLUMN_DURATION + " INTEGER, " +
                Movie.COLUMN_VOTECOUNT + " INTEGER, " +
                Movie.COLUMN_VOTEAVERAGE + " REAL, " +
                Movie.COLUMN_OVERVIEW + " TEXT, " +
                Movie.COLUMN_FAVORITE + " INTEGER, " +
                Movie.COLUMN_POPULAR + " INTEGER, " +
                Movie.COLUMN_TOPRATED + " INTEGER, " +
                Movie.COLUMN_IMAGE + " BLOB, " +
                Movie.COLUMN_POSTER + " BLOB " +
                ")";

        public static final String DROP_TABLE_MOVIE = "DROP TABLE IF EXISTS " + Movie.TABLE_NAME;

        public static final String COUNT_TABLE_MOVIE = "SELECT * FROM " + Movie.TABLE_NAME;

    }

    public final static class VideoMovie implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_BASE_URI, PATH_VIDEOMOVIE);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOMOVIE;
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOMOVIE;

        public static final String TABLE_NAME = "VideoMovie";

        public static final String COLUMN_ID = "id_movie";
        public static final String COLUMN_KEY = "key";


        public static Uri buildVideoMovieWithID(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

        public static Long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static final String CREATE_TABLE_VIDEOMOVIE = "CREATE TABLE " + VideoMovie.TABLE_NAME + " ( " +
                VideoMovie._ID + " INTEGER PRIMARY KEY, " +
                VideoMovie.COLUMN_KEY + " TEXT, " +
                VideoMovie.COLUMN_ID + " INTEGER NOT NULL, " +

                "FOREIGN KEY (" + VideoMovie.COLUMN_ID + ") REFERENCES " + Movie.TABLE_NAME + "(" + Movie.COLUMN_ID + ") ON DELETE CASCADE " +
                ")";

        public static final String DROP_TABLE_VIDEOMOVIE = "DROP TABLE IF EXISTS " + VideoMovie.TABLE_NAME;

        public static final String COUNT_TABLE_VIDEOMOVIE = "SELECT * FROM " + VideoMovie.TABLE_NAME;

    }

    public final static class Serial implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_BASE_URI, PATH_SERIAL);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIAL;
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIAL;

        public static final String TABLE_NAME = "Serial";

        public static final String COLUMN_TITLE = "name";
        public static final String COLUMN_ID = "id_movie";
        public static final String COLUMN_DATERELEASE = "first_air_date";
        public static final String COLUMN_VOTECOUNT = "vote_count";
        public static final String COLUMN_VOTEAVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "over_view";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_POPULAR = "popular";
        public static final String COLUMN_TOPRATED = "top_rated";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_POSTER = "poster";

        public static Uri buildSerialWithID(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

        public static Long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static final String CREATE_TABLE_Serial = "CREATE TABLE " + Serial.TABLE_NAME + " ( " +
                Serial._ID + " INTEGER PRIMARY KEY, "
                + Serial.COLUMN_TITLE + " TEXT NOT NULL, " +
                Serial.COLUMN_ID + " INTEGER NOT NULL UNIQUE, " +
                Serial.COLUMN_DATERELEASE + " TEXT, " +
                Serial.COLUMN_VOTECOUNT + " INTEGER, " +
                Serial.COLUMN_VOTEAVERAGE + " REAL, " +
                Serial.COLUMN_OVERVIEW + " TEXT, " +
                Serial.COLUMN_FAVORITE + " INTEGER, " +
                Serial.COLUMN_POPULAR + " INTEGER, " +
                Serial.COLUMN_TOPRATED + " INTEGER, " +
                Serial.COLUMN_IMAGE + " BLOB, " +
                Serial.COLUMN_POSTER + " BLOB " +
                ")";

        public static final String DROP_TABLE_SERIAL = "DROP TABLE IF EXISTS " + Serial.TABLE_NAME;

        public static final String COUNT_TABLE_SERIAL = "SELECT * FROM " + Serial.TABLE_NAME;

    }

    public final static class Season implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_BASE_URI, PATH_SEASON);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASON;
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASON;

        public static final String TABLE_NAME = "Season";

        public static final String COLUMN_ID = "id_serial";
        public static final String COLUMN_NUMBER = "number_season";


        public static Uri buildSeasonWithID(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

        public static Long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static final String CREATE_TABLE_SEASON = "CREATE TABLE " + Season.TABLE_NAME + " ( " +
                Season._ID + " INTEGER PRIMARY KEY, " +
                Season.COLUMN_NUMBER + " INTEGER, " +
                Season.COLUMN_ID + " INTEGER NOT NULL, " +

                "FOREIGN KEY (" + Season.COLUMN_ID + ") REFERENCES " + Serial.TABLE_NAME + "(" + Serial.COLUMN_ID + ") ON DELETE CASCADE " +
                ")";

        public static final String DROP_TABLE_SEASON = "DROP TABLE IF EXISTS " + Season.TABLE_NAME;

        public static final String COUNT_TABLE_SEASON = "SELECT * FROM " + Season.TABLE_NAME;

    }

    public final static class Epsiode implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_BASE_URI, PATH_EPISODE);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODE;
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODE;

        public static final String TABLE_NAME = "Episode";

        public static final String COLUMN_ID = "id_season";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATERELEASE = "date_release";


        public static Uri buildEpisodeWithID(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

        public static Long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static final String CREATE_TABLE_EPISODE = "CREATE TABLE " + Epsiode.TABLE_NAME + " ( " +
                Epsiode._ID + " INTEGER PRIMARY KEY, " +
                Epsiode.COLUMN_TITLE + " TEXT, " +
                Epsiode.COLUMN_DATERELEASE + " TEXT, " +
                Epsiode.COLUMN_ID + " INTEGER NOT NULL, " +

                "FOREIGN KEY (" + Epsiode.COLUMN_ID + ") REFERENCES " + Season.TABLE_NAME + "(" + Season._ID + ") ON DELETE CASCADE " +
                ")";

        public static final String DROP_TABLE_EPISODE = "DROP TABLE IF EXISTS " + Epsiode.TABLE_NAME;

        public static final String COUNT_TABLE_EPISODE = "SELECT * FROM " + Epsiode.TABLE_NAME;

    }
}

