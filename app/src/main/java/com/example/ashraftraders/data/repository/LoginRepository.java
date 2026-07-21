package com.example.ashraftraders.data.repository;


import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.LoginRequest;
import com.example.ashraftraders.data.model.UserModel;

import java.util.List;

import retrofit2.Call;

public class LoginRepository {

    private final ApiService apiService;

    public LoginRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<List<UserModel>> login(String username, String password) {

        return apiService.login(
                new LoginRequest(username, password)
        );
    }
}