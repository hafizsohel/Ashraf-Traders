package com.example.ashraftraders.data.api;

import com.example.ashraftraders.data.model.BrandModel;
import com.example.ashraftraders.data.model.DashboardModel;
import com.example.ashraftraders.data.model.LoginRequest;
import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.model.SalesSummaryModel;
import com.example.ashraftraders.data.model.UserModel;
import com.example.ashraftraders.data.model.customer.CustomerModel;
import com.example.ashraftraders.data.model.customer.CustomerRequest;
import com.example.ashraftraders.data.model.invoice.InvoiceRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({
            "Content-Type: application/json"
    })
    @POST("rpc/login_user")
    Call<List<UserModel>> login(
            @Body LoginRequest request
    );
    @Headers("Content-Type: application/json")
    @POST("rpc/dashboard_summary")
    Call<List<DashboardModel>> getDashboardSummary();

    @Headers("Content-Type: application/json")
    @POST("rpc/product_list")
    Call<List<ProductModel>> getProductList();

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("products")
    Call<ProductModel> addProduct(
            @Body ProductModel product
    );
    @GET("brands?select=*")
    Call<List<BrandModel>> getBrands();

    @Headers("Content-Type: application/json")
    @POST("rpc/save_invoice")
    Call<Long> saveInvoice(
            @Body InvoiceRequest request
    );

    @Headers("Content-Type: application/json")
    @POST("rpc/search_customer")
    Call<List<CustomerModel>> searchCustomer(
            @Body Map<String, String> body
    );

    @Headers("Content-Type: application/json")
    @POST("rpc/save_customer")
    Call<CustomerModel> saveCustomer(
            @Body CustomerRequest request
    );
    @Headers("Content-Type: application/json")
    @POST("rpc/dashboard_sales_summary")
    Call<List<SalesSummaryModel>> getSalesSummary();
}