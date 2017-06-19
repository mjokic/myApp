package com.example.wifi.myapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.wifi.myapp.asyncTasks.NotificationAsyncTask;


public class NotificationService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationAsyncTask asyncTask = new NotificationAsyncTask();
        asyncTask.execute(getApplicationContext());

        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }
}
