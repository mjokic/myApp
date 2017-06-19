package com.example.wifi.myapp.service.apiCalls;

import com.example.wifi.myapp.model.LoginCredentials;
import com.example.wifi.myapp.model.RegisterCredentials;
import com.example.wifi.myapp.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface UserApiServiceInterface {

    @GET("/users")
    Call<List<User>> getAllUsers();

    @POST("/users")
    Call<User> createUser();

    @POST("/login")
    Call<Void> login(@Body LoginCredentials credentials);

    @POST("/register")
    Call<User> register(@Body RegisterCredentials credentials);

    @Multipart
    @POST("/upload")
    Call<String> uploadUserAvatar(@Part MultipartBody.Part file, @Header("Authorization") String token);

    @Multipart
    @POST("/upload1")
    Call<String> uploadItemPicture(@Part MultipartBody.Part file, @Header("Authorization") String token);

}
