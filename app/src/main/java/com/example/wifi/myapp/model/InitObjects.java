package com.example.wifi.myapp.model;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.Item;
import com.example.wifi.myapp.service.apiCalls.AuctionApiServiceInterface;
import com.example.wifi.myapp.service.apiCalls.BidApiServiceInterface;
import com.example.wifi.myapp.service.apiCalls.ItemApiServiceInterface;
import com.example.wifi.myapp.service.apiCalls.MeApiServiceInterface;
import com.example.wifi.myapp.service.apiCalls.UserApiServiceInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// this class is used to create temp objects
public class InitObjects {
    private static String URL = "http://104.223.21.47/";
//    private static String URL = "http://192.168.0.15:8080/";

    public static String AVATAR_URL = URL + "images/avatar/";
    public static String ITEMS_URL = URL + "images/items/";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(logging);

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();

    private static Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .client(httpClient.build())
            .build();

    public static AuctionApiServiceInterface auctionApiService = retrofit.create(AuctionApiServiceInterface.class);
    public static ItemApiServiceInterface itemApiService = retrofit.create(ItemApiServiceInterface.class);
    public static BidApiServiceInterface bidApiService = retrofit.create(BidApiServiceInterface.class);
    public static UserApiServiceInterface userApiService = retrofit.create(UserApiServiceInterface.class);
    public static MeApiServiceInterface meApiServiceInterface = retrofit.create(MeApiServiceInterface.class);


    private static ProgressDialog progressDialog;
    private static boolean dialogShowed;

    public static void showProgressDialog(Activity activity){
        if(!dialogShowed) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            dialogShowed = true;
        }
    }

    public static void hideProgressDialog(){
        if(dialogShowed) {
            progressDialog.dismiss();
            dialogShowed = false;
        }
    }

}
