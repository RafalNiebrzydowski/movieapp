package com.example.rafal.movieapp.utility;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.example.rafal.movieapp.R;
import com.example.rafal.movieapp.TabActivity;

/**
 * Created by rafal on 14.01.2017.
 */

public class NotificationUtils {
    private static final int MOVIE_NOTIFICATION_ID = 1001;
    public static void createNotifiaction(Context context){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setLargeIcon(BitmapFactory.decodeResource( context.getResources(), R.mipmap.icon_notifi))
                .setContentText(context.getResources().getString(R.string.end_synchronization))
                .setAutoCancel(true);
        Intent mainActivity = new Intent(context, TabActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(mainActivity);
        PendingIntent resultPendingIntent = taskStackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(MOVIE_NOTIFICATION_ID,builder.build());

    }
}
