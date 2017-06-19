package com.example.wifi.myapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.activity.AuctionActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // https://stackoverflow.com/questions/37876257/push-notification-works-incorrectly-when-app-is-on-background-or-not-running/37876727#37876727
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        Map<String, String> data = remoteMessage.getData();
        String auctionId = data.get("auctionId");

        RemoteMessage.Notification notification = remoteMessage.getNotification();

//        showNotification(notification.getTitle(), notification.getBody(), auctionId);
        showNotification(data.get("title"), data.get("body"), auctionId);

    }

    private void showNotification(String title, String message, String auctionId){
        System.out.println("In showNotification " + auctionId + " <-- Auction id");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String token = prefs.getString("token", null);
        Long userId = prefs.getLong("userId", 0);
        System.out.println(userId + " <-- userId line 45");

        Intent myIntent = new Intent(getApplicationContext(), AuctionActivity.class);
        if(auctionId != null) {
            myIntent.putExtra("auctionId", Long.parseLong(auctionId));
            myIntent.putExtra("token", token);
            myIntent.putExtra("userId", userId);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                myIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder =
                new Notification.Builder(this);
        builder.setContentTitle(title)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText(message)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[] {  0, 500, 250, 500  })
                .setAutoCancel(true);

        if(auctionId != null){
            builder.setContentIntent(pendingIntent);
        }


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

    }

}
