package com.example.wifi.myapp.service.apiCalls;

import com.example.wifi.myapp.model.DTO.ItemDTO;
import com.example.wifi.myapp.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ItemApiServiceInterface {

    @GET("items/")
    Call<List<Item>> getAllItems(@Header("Authorization") String token);

    @GET("items/{id}")
    Call<Item> getItemById(@Path("id") long id, @Header("Authorization") String token);

    @POST("items/")
    Call<ItemDTO> addItem(@Body ItemDTO itemDTO, @Header("Authorization") String token);

    @PUT("items/{id}")
    Call<ItemDTO> editItem(@Body ItemDTO itemDTO, @Path("id") long id, @Header("Authorization") String token);

    @DELETE("items/{id}")
    Call<Void> deleteItem(@Path("id") long id, @Header("Authorization") String token);
}
