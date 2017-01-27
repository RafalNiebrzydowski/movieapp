package com.example.rafal.movieapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rafal.movieapp.adapter.movieAdapter.InternetMovieAdapter;
import com.example.rafal.movieapp.adapter.serialAdapter.InternetSerialAdapter;
import com.example.rafal.movieapp.adapter.movieAdapter.MovieAdapter;
import com.example.rafal.movieapp.adapter.serialAdapter.SerialAdapter;
import com.example.rafal.movieapp.data.MovieContract;
import com.example.rafal.movieapp.utility.PreferenceUtils;

public class TabActivity extends AppCompatActivity implements ActionBar.TabListener,
        MovieAdapter.MovieAdapterOnClickHandler, SerialAdapter.SerialAdapterOnClickHandler,
        InternetMovieAdapter.MovieAdapterOnClickHandler, InternetSerialAdapter.SerialAdapterOnClickHandler {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private static String mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mTheme = sharedPreferences.getString(getString(R.string.pref_style_key), getString(R.string.pref_style_default));
        PreferenceUtils.changeStyle(sharedPreferences,this, mTheme);
        setContentView(R.layout.activity_tab);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int nr = extras.getInt("pos");
            switchTab(nr);
        }
    }

    public void switchTab(int tab){
        mViewPager.setCurrentItem(tab);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = PreferenceUtils.changeStyle(sharedPreferences,this,mTheme);
        if(theme!=null){
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



    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
    @Override
    public void onClick(long _id) {
        Intent movieDetailIntent = new Intent(TabActivity.this, DetailActivity.class);
        Uri uriForDateClicked = MovieContract.Movie.buildMovieWithID(_id);
        movieDetailIntent.setData(uriForDateClicked);
        startActivity(movieDetailIntent);
    }
    @Override
    public void onClickSerial(long _id) {
        Intent movieDetailIntent = new Intent(TabActivity.this, DetailSerialActivity.class);
        Uri uriForDateClicked = MovieContract.Serial.buildSerialWithID(_id);
        movieDetailIntent.setData(uriForDateClicked);
        startActivity(movieDetailIntent);
    }
    @Override
    public void onClickMovie(long _id) {
        Intent movieDetailIntent = new Intent(TabActivity.this, DetailInternetActivity.class);
        movieDetailIntent.putExtra(Intent.EXTRA_TEXT, _id);
        startActivity(movieDetailIntent);
    }
    @Override
    public void onClickSerialI(long _id) {
        Intent movieDetailIntent = new Intent(TabActivity.this, DetailSerialInternetActivity.class);
        movieDetailIntent.putExtra(Intent.EXTRA_TEXT, _id);
        startActivity(movieDetailIntent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MainFragment();
                case 1:
                    return new FavoriteFragment();
                case 2:
                    return new MovieFragment();
                case 3:
                    return new SerialFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Main";
                case 1:
                    return "Favorite";
                case 2:
                    return "Movies";
                case 3:
                    return "Serials";
            }
            return null;
        }
    }

}
