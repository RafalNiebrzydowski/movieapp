package com.example.rafal.movieapp.widget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.data.MovieContract;

/**
 * Created by rafal on 25.01.2017.
 */

public class MovieAppWidgetIntentService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {}

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(MovieContract.Movie.CONTENT_URI, null, MovieContract.Movie.COLUMN_POPULAR + " = ?",
                        new String[]{String.valueOf(1)}, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                int movieId = data.getInt(data.getColumnIndex(MovieContract.Movie.COLUMN_ID));
                String title = data.getString(data.getColumnIndex(MovieContract.Movie.COLUMN_TITLE));
                byte[] image = data.getBlob(data.getColumnIndex(MovieContract.Movie.COLUMN_IMAGE));

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.movie_app_widget_list_item);

                views.setTextViewText(R.id.title_movie_textView, title);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                views.setImageViewBitmap(R.id.poster_movie_imageView, bitmap);
                final Intent launchIntent = new Intent();
                Uri uri = MovieContract.Movie.buildMovieWithID(movieId);
                launchIntent.setData(uri);
                views.setOnClickFillInIntent(R.id.poster_movie_imageView, launchIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.movie_app_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(data.getColumnIndex(MovieContract.Movie.COLUMN_ID));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}

