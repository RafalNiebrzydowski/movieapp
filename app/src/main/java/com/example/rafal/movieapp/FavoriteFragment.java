package com.example.rafal.movieapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.rafal.movieapp.adapter.movieAdapter.MovieAdapter;
import com.example.rafal.movieapp.adapter.serialAdapter.SerialAdapter;
import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.utility.Utility;

public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_MOVIE_LOADER = 8;
    private static final int ID_SERIAL_LOADER = 9;
    private MovieAdapter mMovieAdapter;
    private SerialAdapter mSerialAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerSerialView;
    private ProgressBar mLoadingIndicatorMovie;
    private ProgressBar mLoadingIndicatorSerial;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.popular_movies_list);
        mRecyclerSerialView = (RecyclerView) rootView.findViewById(R.id.toprated_movies_list);
        mMovieAdapter = new MovieAdapter(getActivity(), (MovieAdapter.MovieAdapterOnClickHandler) getActivity(), rootView.findViewById(R.id.emptyView));
        mSerialAdapter = new SerialAdapter(getActivity(), (SerialAdapter.SerialAdapterOnClickHandler) getActivity(), rootView.findViewById(R.id.emptyViewSerial));
        Utility.configureRecyclerView(mRecyclerView, getActivity(), mMovieAdapter);
        Utility.configureRecyclerView(mRecyclerSerialView, getActivity(), mSerialAdapter);
        mLoadingIndicatorMovie = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_movie);
        mLoadingIndicatorSerial = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_top_movie);
        Utility.showLoading(mRecyclerView,mLoadingIndicatorMovie);
        Utility.showLoading(mRecyclerSerialView,mLoadingIndicatorSerial);
       

        getLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
        getLoaderManager().initLoader(ID_SERIAL_LOADER, null, this);

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_MOVIE_LOADER: {
                Uri movieQueryUri = MovieContract.Movie.CONTENT_URI;
                String selection = MovieContract.Movie.COLUMN_FAVORITE + " =?";
                String[] selectionArgs = new String[]{String.valueOf(1)};
                return new CursorLoader(getActivity(),
                        movieQueryUri,
                        null,
                        selection,
                        selectionArgs,
                        null);
            }
            case ID_SERIAL_LOADER: {
                Uri serialQueryUri = MovieContract.Serial.CONTENT_URI;
                String selection = MovieContract.Serial.COLUMN_FAVORITE + " =?";
                String[] selectionArgs = new String[]{String.valueOf(1)};
                return new CursorLoader(getActivity(),
                        serialQueryUri,
                        null,
                        selection,
                        selectionArgs,
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {

            case ID_MOVIE_LOADER: {
                mMovieAdapter.swapCursor(data);
                if (data.getCount() != 0)
                    Utility.showViews(mRecyclerView, mLoadingIndicatorMovie);
                else
                    mLoadingIndicatorMovie.setVisibility(View.GONE);
                break;
            }

            case ID_SERIAL_LOADER: {
                mSerialAdapter.swapCursor(data);

                if (data.getCount() != 0)
                    Utility.showViews(mRecyclerSerialView, mLoadingIndicatorSerial);
                else
                    mLoadingIndicatorSerial.setVisibility(View.GONE);
                break;
            }

            default:
                throw new RuntimeException("Loader Not Implemented: " + loader.getId());
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {

            case ID_MOVIE_LOADER:
                mMovieAdapter.swapCursor(null);
                break;

            case ID_SERIAL_LOADER:
                mMovieAdapter.swapCursor(null);
                break;

            default:
                throw new RuntimeException("Loader Not Implemented: " + loader.getId());
        }

    }

}
