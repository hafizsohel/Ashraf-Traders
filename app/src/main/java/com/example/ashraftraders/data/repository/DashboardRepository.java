package com.example.ashraftraders.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.DashboardModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {

    private static final String TAG = "DashboardRepository";

    private final ApiService apiService;

    public DashboardRepository() {
        apiService = ApiClient
                .getClient()
                .create(ApiService.class);
    }

    public void getDashboardSummary(MutableLiveData<List<DashboardModel>> liveData) {

        apiService.getDashboardSummary().enqueue(new Callback<List<DashboardModel>>() {

            @Override
            public void onResponse(Call<List<DashboardModel>> call,
                                   Response<List<DashboardModel>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && !response.body().isEmpty()) {

                    Log.d(TAG, "Dashboard = " + response.body());

                    liveData.postValue(response.body());

                } else {

                    Log.e(TAG, "Response Error : " + response.code());

                }

            }

            @Override
            public void onFailure(Call<List<DashboardModel>> call,
                                  Throwable t) {

                Log.e(TAG, "API Error", t);

            }
        });

    }

}