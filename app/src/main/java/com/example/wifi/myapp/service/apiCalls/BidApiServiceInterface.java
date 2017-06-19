package com.example.wifi.myapp.service.apiCalls;

import com.example.wifi.myapp.model.Bid;
import com.example.wifi.myapp.model.DTO.BidDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface BidApiServiceInterface {

    @GET("/bids")
    Call<List<Bid>> getAllBids();

    @POST("/bids/")
    Call<Bid> createBid(@Body BidDTO bid, @Header("Authorization") String token);


}
