package com.example.wifi.myapp.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class NotificationAsyncTask extends AsyncTask<Context, String, Context> {


    @Override
    protected Context doInBackground(Context... params) {
        try{
            Thread.sleep(1000);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return params[0];
    }

    @Override
    protected void onPostExecute(Context context) {
        super.onPostExecute(context);
        Intent intent1 = new Intent("BROADCAST_EVENT");
        context.sendBroadcast(intent1);
    }
}
