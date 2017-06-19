package com.example.wifi.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.wifi.myapp.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting preference object
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String token = prefs.getString("token", null);
        final long userId = prefs.getLong("userId", 0);
        System.out.println("Linija 20 splashScreenActivity " + userId + " <-- userId");

        if(getIntent().hasExtra("auctionId")){
            long auctionId = Long.parseLong(getIntent().getStringExtra("auctionId"));
            System.out.println(auctionId + " <-- Auction ID from notification");

            startAuctionActivity(token, userId, auctionId);
            finish();
            return;
        }

        boolean displaySplash = prefs.getBoolean("pref_splash_vis", true);
        if(!displaySplash){
            startNextActivity(token, userId);
            return;
        }


        String timeoutTmp = prefs.getString("pref_splash_time", "5");
        final int timeout = Integer.parseInt(timeoutTmp);
        if(timeout < 1){ startNextActivity(token, userId); return;}


        setContentView(R.layout.activity_splash_screen);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeout * 1000);
                    startNextActivity(token, userId);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }

            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {}

    private void startNextActivity(String token, long userId){
        System.out.println("User id: " + userId + " SplashScreenActivity");

        Intent intent;
        if(token == null){
             intent = new Intent(this, LoginActivity.class);
        }else{
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("userId", userId);
        }
        startActivity(intent);
    }


    private void startAuctionActivity(String token, long userId, long auctionId){
        // called from notification
        Intent intent = new Intent(this, AuctionActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("userId", userId);
        intent.putExtra("auctionId", auctionId);
        startActivity(intent);
    }
}
