package com.example.wifi.myapp.broadcastReciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("The highest bidder!")
                .setContentText("Yor're the highest bidder!")
                .setSmallIcon(android.R.mipmap.sym_def_app_icon);

        NotificationManager manager = (NotificationManager)
                context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
