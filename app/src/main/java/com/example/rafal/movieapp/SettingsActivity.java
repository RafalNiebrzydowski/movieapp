package com.example.rafal.movieapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.rafal.movieapp.utility.PreferenceUtils;

/**
 * Created by rafal on 13.01.2017.
 */

public class SettingsActivity extends AppCompatActivity {
    private static String mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mTheme = sharedPreferences.getString(getString(R.string.pref_style_key), getString(R.string.pref_style_default));
        PreferenceUtils.changeStyle(sharedPreferences,this, mTheme);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
}