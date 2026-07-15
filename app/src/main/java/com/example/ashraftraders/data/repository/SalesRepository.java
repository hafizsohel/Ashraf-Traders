package com.example.ashraftraders.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.SalesSummaryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesRepository {

    private final ApiService apiService;

    public SalesRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public interface SummaryListener {
        void onSuccess(SalesSummaryModel model);
        void onError(String error);
    }

    public void getSalesSummary(SummaryListener listener) {

        apiService.getSalesSummary().enqueue(new Callback<List<SalesSummaryModel>>() {

            @Override
            public void onResponse(@NonNull Call<List<SalesSummaryModel>> call,
                                   @NonNull Response<List<SalesSummaryModel>> response) {

                Log.d("SALES_SUMMARY", "HTTP = " + response.code());

                for (SalesSummaryModel m : response.body()) {

                    Log.d("SUMMARY",
                            "Sale=" + m.getTotalSale());

                    Log.d("SUMMARY",
                            "Order=" + m.getTotalOrder());

                    Log.d("SUMMARY",
                            "Due=" + m.getTotalDue());

                    Log.d("SUMMARY",
                            "Profit=" + m.getTotalProfit());
                }

                if (response.isSuccessful()
                        && response.body() != null
                        && !response.body().isEmpty()) {

                    SalesSummaryModel model = response.body().get(0);

                    Log.d("SALES_SUMMARY",
                            "Sale = " + model.getTotalSale()
                                    + " Order = " + model.getTotalOrder()
                                    + " Due = " + model.getTotalDue()
                                    + " Profit = " + model.getTotalProfit());

                    listener.onSuccess(model);

                } else {

                    Log.e("SALES_SUMMARY", "No data");

                    listener.onError("No data found");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SalesSummaryModel>> call,
                                  @NonNull Throwable t) {

                listener.onError(t.getMessage());
            }
        });
    }
}