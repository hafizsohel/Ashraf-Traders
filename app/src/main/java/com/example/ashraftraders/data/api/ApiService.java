package com.example.ashraftraders.data.api;


import com.example.ashraftraders.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {

    @Headers({
            "Content-Type: application/json"
    })
    @GET("users")
    Call<List<User>> login(
            @Query("username") String username,
            @Query("password") String password,
            @Query("status") String status
    );
}