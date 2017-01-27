package com.example.rafal.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rafal on 13.01.2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private final static int SQL_DATABASE_VERSION = 1;
    private final static String SQL_DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, SQL_DATABASE_NAME, null, SQL_DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MovieContract.Movie.CREATE_TABLE_MOVIE);
        sqLiteDatabase.execSQL(MovieContract.VideoMovie.CREATE_TABLE_VIDEOMOVIE);
        sqLiteDatabase.execSQL(MovieContract.Serial.CREATE_TABLE_Serial);
        sqLiteDatabase.execSQL(MovieContract.Season.CREATE_TABLE_SEASON);
        sqLiteDatabase.execSQL(MovieContract.Epsiode.CREATE_TABLE_EPISODE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(MovieContract.Movie.DROP_TABLE_MOVIE);
        sqLiteDatabase.execSQL(MovieContract.VideoMovie.DROP_TABLE_VIDEOMOVIE);
        sqLiteDatabase.execSQL(MovieContract.Serial.DROP_TABLE_SERIAL);
        sqLiteDatabase.execSQL(MovieContract.Season.DROP_TABLE_SEASON);
        sqLiteDatabase.execSQL(MovieContract.Epsiode.DROP_TABLE_EPISODE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
