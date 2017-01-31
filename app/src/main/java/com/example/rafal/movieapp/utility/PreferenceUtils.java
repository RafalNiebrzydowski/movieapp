package com.example.rafal.movieapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rafal.movieapp.R;


/**
 * Created by rafal on 14.01.2017.
 */

public class PreferenceUtils
{

    public static String changeStyle(SharedPreferences sharedPreferences, Context context, String defaultTheme){

        String themeName = sharedPreferences.getString(context.getString(R.string.pref_style_key), context.getString(R.string.pref_style_default));
        if (themeName.equals("dark")) {
            context.setTheme(R.style.AppTheme);
        } else if (themeName.equals("light")) {
            context.setTheme(R.style.AppTheme2);
        }
        if(!themeName.equals(defaultTheme))
            return themeName;
        else
            return null;
    }
    public static String changeStyleSettings(SharedPreferences sharedPreferences, Context context, String defaultTheme){

        String themeName = sharedPreferences.getString(context.getString(R.string.pref_style_key), context.getString(R.string.pref_style_default));
        if (themeName.equals("dark")) {
            context.setTheme(R.style.AppThemeSettings);
        } else if (themeName.equals("light")) {
            context.setTheme(R.style.AppThemeSettings2);
        }
        if(!themeName.equals(defaultTheme))
            return themeName;
        else
            return null;
    }
}
