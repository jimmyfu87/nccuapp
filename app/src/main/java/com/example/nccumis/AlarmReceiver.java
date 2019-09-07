package com.example.nccumis;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import androidx.annotation.RequiresApi;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;

public class AlarmReceiver extends BroadcastReceiver{
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, AutocrawlService.class));
        } else {
            context.startService(new Intent(context, Home.class));
        }
    }
}