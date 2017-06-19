package com.example.wifi.myapp.service.apiCalls;

import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.Bid;
import com.example.wifi.myapp.model.DTO.AuctionDTO;
import com.example.wifi.myapp.model.DTO.BidDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuctionApiServiceInterface {

    @GET("auctions/")
    Call<List<Auction>> getAllAuctions(@Header("Authorization") String token);

    @GET("auctions/{id}")
    Call<Auction> getAuction(@Path("id") long id, @Header("Authorization") String token);

    @GET("auctions/{id}/bids")
    Call<List<Bid>> getBids(@Path("id") long id, @Header("Authorization") String token);

    @GET("auctions/{id}/bids")
    Call<List<BidDTO>> getBidsDTO(@Path("id") long id, @Header("Authorization") String token);

    @POST("auctions/")
    Call<Auction> createAuction(@Body AuctionDTO auctionDTO, @Header("Authorization") String token);

    @PUT("auctions/{id}")
    Call<Auction> editAuction(@Body AuctionDTO auctionDTO, @Path("id") long auctionId, @Header("Authorization") String token);

    @DELETE("auctions/{id}")
    Call<Void> deleteAuction(@Path("id") long auctionId, @Header("Authorization") String token);

}
