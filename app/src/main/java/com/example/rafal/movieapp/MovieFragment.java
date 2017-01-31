package com.example.rafal.movieapp;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.rafal.movieapp.adapter.movieAdapter.InternetMovieAdapter;
import com.example.rafal.movieapp.data.model.Movie;
import com.example.rafal.movieapp.sync.task.movieTask.FetchMovieTask;
import com.example.rafal.movieapp.utility.Utility;

import java.util.List;

public class MovieFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movie>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static String mTheme;
    private FetchMovieTask movies;
    private InternetMovieAdapter internetMovieAdapter;
    private int position;
    Button searchButton;
    RecyclerView mRecyclerView;
    ProgressBar mLoadingIndicatorMovie;
    private static String sortBy = "sort_by=popularity.desc&";
    private boolean isSearch = false;

    StringBuilder stringBuilderQuery;
    int datasize = 0;
    View emptyView;
    private static final int ID_MOVIE_LOADER = 6;
    private IntentFilter filter =
            new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    private BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (Utility.isOnline(arg0)) {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                if (internetMovieAdapter.getItemCount() == 0)
                    initLoader();
            } else {
                mRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = 1;

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        final View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        emptyView = (View) rootView.findViewById(R.id.emptyView);
        String sort = "sort_by=";
        stringBuilderQuery = new StringBuilder("");
        StringBuilder builder = new StringBuilder(sort);
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
        builder.append(sharedPreferences.getString(getActivity().getString(R.string.pref_key_sort_movie), getActivity().getString(R.string.pref_default_sort_movie)) + "&");
        sortBy = builder.toString();
        mLoadingIndicatorMovie = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_movie);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.popular_movies_list);
        internetMovieAdapter = new InternetMovieAdapter(getActivity(), (InternetMovieAdapter.MovieAdapterOnClickHandler) getActivity());
        Utility.configureVerticalRecyclerView(mRecyclerView,getActivity(),internetMovieAdapter);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        searchButton.setBackgroundColor(color);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = 1;
                isSearch = false;
                internetMovieAdapter.clearCursor();
                resetLoader();
                searchButton.setVisibility(View.GONE);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == recyclerView.SCROLL_STATE_DRAGGING) {


                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) //check for scroll down
                {
                    if (internetMovieAdapter.getItemCount() > 0) {
                        if (Utility.isOnline(rootView.getContext())) {
                            position += 1;
                            resetLoader();
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        setHasOptionsMenu(true);
        return rootView;
    }

    public void initLoader() {
        getLoaderManager().initLoader(ID_MOVIE_LOADER, null, this).forceLoad();
    }

    public void resetLoader() {
        getLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this).forceLoad();
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main_fragment_movie, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMovie);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(Utility.isOnline(getContext())) {
                    stringBuilderQuery = new StringBuilder(query);
                    isSearch = true;
                    position = 1;
                    internetMovieAdapter.clearCursor();
                    resetLoader();

                    searchButton.setVisibility(View.VISIBLE);

                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case ID_MOVIE_LOADER: {
                mLoadingIndicatorMovie.setVisibility(View.VISIBLE);
                return new FetchMovieTask(getActivity(), position, sortBy, isSearch, stringBuilderQuery.toString().replace(" ", "%20"));
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        switch (loader.getId()) {

            case ID_MOVIE_LOADER: {
                internetMovieAdapter.setMovies(data);
                internetMovieAdapter.notifyDataSetChanged();
                if (data != null) {
                    if (data.size() != 0) {
                        datasize += (data.size() + 1);
                        mLoadingIndicatorMovie.setVisibility(View.INVISIBLE);
                    }
                    if (data.size() <= 0 && position > 1) {
                        mLoadingIndicatorMovie.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            }

            default:
                throw new RuntimeException("Loader Not Implemented: " + loader.getId());
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
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        switch (loader.getId()) {

            case ID_MOVIE_LOADER:
                break;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loader.getId());
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_key_sort_movie))) {
            String sort = "sort_by=";
            StringBuilder builder = new StringBuilder(sort);
            builder.append(sharedPreferences.getString(getActivity().getString(R.string.pref_key_sort_movie), getActivity().getString(R.string.pref_default_sort_movie)) + "&");
            sortBy = builder.toString();
            internetMovieAdapter.clearCursor();
            resetLoader();
        }
    }

}
