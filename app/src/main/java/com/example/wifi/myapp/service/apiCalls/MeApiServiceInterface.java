package com.example.wifi.myapp.service.apiCalls;

import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;


public interface MeApiServiceInterface {

    @GET("/me")
    Call<User> getMe(@Header("Authorization") String token);

    @GET("/me/auctions")
    Call<List<Auction>> getMyAuctions(@Header("Authorization") String token);

    @PUT("/me")
    Call<User> updateMe(@Body User user, @Header("Authorization") String token);

}
