package com.example.ashraftraders.data.api;

import com.example.ashraftraders.data.model.DashboardModel;
import com.example.ashraftraders.data.model.LoginRequest;
import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({
            "Content-Type: application/json"
    })
    @POST("rpc/login_user")
    Call<List<User>> login(
            @Body LoginRequest request
    );
    @Headers("Content-Type: application/json")
    @POST("rpc/dashboard_summary")
    Call<List<DashboardModel>> getDashboardSummary();

    @Headers("Content-Type: application/json")
    @POST("rpc/product_list")
    Call<List<ProductModel>> getProductList();

    // ১. সরাসরি টেবিল রাউটে পোস্ট করা (যদি টেবিলের নাম products হয়ে থাকে)
    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation" // ইনসার্ট করার পর যেন নতুন তৈরি হওয়া ডেটা অবজেক্টটি ব্যাক করে
    })
    @POST("products") // আপনার Supabase টেবিলের নাম এখানে দিন (যেমন products)
    Call<ProductModel> addProduct(@Body ProductModel product);
}