package com.example.ashraftraders.data.repository;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.DueModel;
import com.google.gson.JsonObject;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DueRepository {

    private final ApiService apiService;

    public DueRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void getDueInvoiceList(MutableLiveData<List<DueModel>> liveData) {

        JsonObject body = new JsonObject();

        apiService.getDueInvoiceList(body).enqueue(new Callback<List<DueModel>>() {

            @Override
            public void onResponse(Call<List<DueModel>> call,
                                   Response<List<DueModel>> response) {
                Log.d("DueRefresh", "API Called");

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("DueRefresh", "Size = " + response.body().size());
                    liveData.setValue(response.body());

                    List<DueModel> list = response.body();
                    for (DueModel item : list) {

                        Log.d("DueRefresh",
                                "Invoice ID = " + item.getInvoiceId()
                                        + " | Invoice No = " + item.getInvoiceNo()
                                        + " | Due = " + item.getDueAmount());

                    }

                }
            }

            @Override
            public void onFailure(Call<List<DueModel>> call, Throwable t) {

            }
        });

    }

}