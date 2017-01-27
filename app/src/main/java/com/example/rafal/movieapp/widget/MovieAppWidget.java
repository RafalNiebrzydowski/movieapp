package com.example.rafal.movieapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.rafal.movieapp.DetailActivity;
import com.example.rafal.movieapp.MainFragment;
import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.TabActivity;
import com.example.rafal.movieapp.sync.MovieSyncUtils;

/**
 * Implementation of App Widget functionality.
 */
public class MovieAppWidget extends AppWidgetProvider {
    private static final String ACTION_UPDATE_CLICK =
            "com.example.rafal.movieapp.action.UPDATE_CLICK";
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movie_app_widget);
            Intent intent = new Intent(context, TabActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            } else {
                setRemoteAdapterV11(context, views);
            }
            boolean useDetailActivity = context.getResources()
                    .getBoolean(R.bool.use_detail_activity);
            Intent clickIntentTemplate = useDetailActivity
                    ? new Intent(context, DetailActivity.class)
                    : new Intent(context, MainFragment.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.stackWidgetView, clickPendingIntentTemplate);
            views.setEmptyView(R.id.stackWidgetView, R.id.widget_empty);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(@NonNull final Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (MovieSyncUtils.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stackWidgetView);
        }

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.stackWidgetView,
                new Intent(context, MovieAppWidgetIntentService.class));
    }


    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.stackWidgetView,
                new Intent(context, MovieAppWidgetIntentService.class));
    }


}