package com.example.rafal.movieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.rafal.movieapp.data.MovieDbHelper;
import com.example.rafal.movieapp.sync.MovieSyncUtils;
import com.example.rafal.movieapp.utility.Utility;

public class MainFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ID_MOVIE_LOADER = 0;
    private static final int ID_MOVIE_TOP_RATED_LOADER = 1;
    private static final int ID_SERIAL_LOADER = 2;
    private static final int ID_SERIAL_TOP_RATED_LOADER = 3;
    private MovieAdapter mMovieAdapter;
    private MovieAdapter mMovieTopRatedAdapter;
    private View contentView;
    private SerialAdapter mSerialAdapter;
    private SerialAdapter mSerialTopRatedAdapter;


    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerTopRatedView;

    private RecyclerView mRecyclerSerialView;
    private RecyclerView mRecyclerSerialTopRatedView;
    View emptyView;
    private ProgressBar mLoadingIndicatorMovie;
    private ProgressBar mLoadingIndicatorTopMovie;
    private ProgressBar mLoadingIndicatorSerial;
    private ProgressBar mLoadingIndicatorTopSerial;
    private IntentFilter filter =
            new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private BroadcastReceiver broadcast = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (Utility.isOnline(arg0)){
                contentView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                if(mMovieAdapter.getItemCount()==0 && mMovieTopRatedAdapter.getItemCount()==0 && mSerialAdapter.getItemCount()==0 && mSerialTopRatedAdapter.getItemCount()==0)
                    MovieSyncUtils.initialize(getActivity());

            }
            else{
                MovieDbHelper movieDbHelper = new MovieDbHelper(arg0);
                if(movieDbHelper.getReadableDatabase().rawQuery(MovieContract.Movie.COUNT_TABLE_MOVIE,null).getCount()<=0) {
                    if (mMovieAdapter.getItemCount() == 0 && mMovieTopRatedAdapter.getItemCount() == 0 && mSerialAdapter.getItemCount() == 0 && mSerialTopRatedAdapter.getItemCount() == 0) {
                        contentView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    contentView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }

        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.popular_movies_list);
        mMovieAdapter = new MovieAdapter(rootView.getContext(), (MovieAdapter.MovieAdapterOnClickHandler) getActivity());
        Utility.configureRecyclerView(mRecyclerView, getActivity(), mMovieAdapter);

        mRecyclerTopRatedView = (RecyclerView) rootView.findViewById(R.id.toprated_movies_list);
        mMovieTopRatedAdapter = new MovieAdapter(rootView.getContext(), (MovieAdapter.MovieAdapterOnClickHandler) rootView.getContext());
        Utility.configureRecyclerView(mRecyclerTopRatedView, getActivity(), mMovieTopRatedAdapter);

        mRecyclerSerialView = (RecyclerView) rootView.findViewById(R.id.popular_serials_list);
        mSerialAdapter = new SerialAdapter(rootView.getContext(),(SerialAdapter.SerialAdapterOnClickHandler)  rootView.getContext());
        Utility.configureRecyclerView(mRecyclerSerialView, getActivity(), mSerialAdapter);

        mRecyclerSerialTopRatedView = (RecyclerView) rootView.findViewById(R.id.toprated_serials_list);
        mSerialTopRatedAdapter = new SerialAdapter(rootView.getContext(),(SerialAdapter.SerialAdapterOnClickHandler) rootView.getContext());
        Utility.configureRecyclerView(mRecyclerSerialTopRatedView, getActivity(), mSerialTopRatedAdapter);

        contentView = (View) rootView.findViewById(R.id.content_list) ;
        emptyView = (View) rootView.findViewById(R.id.emptyView);
        mLoadingIndicatorMovie = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_movie);
        mLoadingIndicatorTopMovie = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_top_movie);
        mLoadingIndicatorSerial = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_serial);
        mLoadingIndicatorTopSerial = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_top_serial);
        Utility.showLoading(mRecyclerView, mLoadingIndicatorMovie);
        Utility.showLoading(mRecyclerTopRatedView, mLoadingIndicatorTopMovie);
        Utility.showLoading(mRecyclerSerialView, mLoadingIndicatorSerial);
        Utility.showLoading(mRecyclerSerialTopRatedView, mLoadingIndicatorTopSerial);




        getLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
        getLoaderManager().initLoader(ID_MOVIE_TOP_RATED_LOADER, null, this);
        getLoaderManager().initLoader(ID_SERIAL_LOADER, null, this);
        getLoaderManager().initLoader(ID_SERIAL_TOP_RATED_LOADER, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_MOVIE_LOADER: {
                Uri movieQueryUri = MovieContract.Movie.CONTENT_URI;

                String selection = MovieContract.Movie.COLUMN_POPULAR + " =?";
                String[] selectionArgs = new String[]{String.valueOf(1)};
                return new CursorLoader(getActivity(),
                        movieQueryUri,
                        null,
                        selection,
                        selectionArgs,
                        null);
            }
            case ID_MOVIE_TOP_RATED_LOADER: {
                Uri movieQueryUri = MovieContract.Movie.CONTENT_URI;

                String selection = MovieContract.Movie.COLUMN_TOPRATED + " =?";
                String[] selectionArgs = new String[]{String.valueOf(1)};
                return new CursorLoader(getActivity(),
                        movieQueryUri,
                        null,
                        selection,
                        selectionArgs,
                        null);
            }
            case ID_SERIAL_LOADER: {
                Uri movieQueryUri = MovieContract.Serial.CONTENT_URI;

                String selection = MovieContract.Serial.COLUMN_POPULAR + " =?";
                String[] selectionArgs = new String[]{String.valueOf(1)};
                return new CursorLoader(getActivity(),
                        movieQueryUri,
                        null,
                        selection,
                        selectionArgs,
                        null);
            }
            case ID_SERIAL_TOP_RATED_LOADER: {
                Uri movieQueryUri = MovieContract.Serial.CONTENT_URI;

                String selection = MovieContract.Serial.COLUMN_TOPRATED + " =?";
                String[] selectionArgs = new String[]{String.valueOf(1)};
                return new CursorLoader(getActivity(),
                        movieQueryUri,
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
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcast, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcast);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {

            case ID_MOVIE_LOADER:{
                mMovieAdapter.swapCursor(data);
                if (mMovieAdapter.getItemCount() != 0)
                    Utility.showViews(mRecyclerView, mLoadingIndicatorMovie);
            break;}
            case ID_MOVIE_TOP_RATED_LOADER: {
                mMovieTopRatedAdapter.swapCursor(data);
                if (mMovieTopRatedAdapter.getItemCount() != 0)
                    Utility.showViews(mRecyclerTopRatedView, mLoadingIndicatorTopMovie);

                break;
            }
            case ID_SERIAL_LOADER: {
                mSerialAdapter.swapCursor(data);
                if (mSerialAdapter.getItemCount() != 0)
                    Utility.showViews(mRecyclerSerialView, mLoadingIndicatorSerial);
                break;
            }
            case ID_SERIAL_TOP_RATED_LOADER: {
                mSerialTopRatedAdapter.swapCursor(data);
                if (mSerialTopRatedAdapter.getItemCount() != 0)
                    Utility.showViews(mRecyclerSerialTopRatedView, mLoadingIndicatorTopSerial);

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
            case ID_MOVIE_TOP_RATED_LOADER:
                mMovieTopRatedAdapter.swapCursor(null);
                break;
            case ID_SERIAL_LOADER:
                mMovieAdapter.swapCursor(null);
                break;
            case ID_SERIAL_TOP_RATED_LOADER:
                mMovieTopRatedAdapter.swapCursor(null);
                break;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loader.getId());
        }

    }

}
