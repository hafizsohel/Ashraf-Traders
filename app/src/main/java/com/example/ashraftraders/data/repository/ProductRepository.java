package com.example.ashraftraders.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private static final String TAG = "ProductRepository";

    private final ApiService apiService;

    public ProductRepository() {
        apiService = ApiClient
                .getClient()
                .create(ApiService.class);
    }

    public void getProducts(MutableLiveData<List<ProductModel>> liveData) {

        apiService.getProductList().enqueue(new Callback<List<ProductModel>>() {

            @Override
            public void onResponse(Call<List<ProductModel>> call,
                                   Response<List<ProductModel>> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    Log.d(TAG, "Products : " + response.body().size());

                    liveData.postValue(response.body());

                } else {

                    Log.e(TAG, "Response Code : " + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call,
                                  Throwable t) {

                Log.e(TAG, t.getMessage(), t);

            }
        });

    }

}