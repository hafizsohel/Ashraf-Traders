package com.example.ashraftraders.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.InvoiceReturnModel;
import com.example.ashraftraders.data.model.SaleModel;
import com.example.ashraftraders.data.model.SaleReturnItemModel;
import com.example.ashraftraders.data.model.SalesSummaryModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
    public interface SaleReturnCallback {
        void onSuccess();
        void onError(String message);
    }
    public interface SearchInvoiceCallback {

        void onSuccess(
                InvoiceReturnModel invoice,
                List<SaleReturnItemModel> items
        );

        void onError(String message);

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

    public void getRecentSales(MutableLiveData<List<SaleModel>> liveData) {

        apiService.getRecentSales().enqueue(new Callback<List<SaleModel>>() {

            @Override
            public void onResponse(@NonNull Call<List<SaleModel>> call,
                                   @NonNull Response<List<SaleModel>> response) {

                if (response.body() != null) {

                    for (SaleModel model : response.body()) {
                        Log.d("RECENT", model.toString());
                    }

                    liveData.postValue(response.body());
                } else {
                    Log.d("RECENT", "Body = null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SaleModel>> call,
                                  @NonNull Throwable t) {

                Log.e("RECENT_SALES", "API Error", t);

                liveData.postValue(null);
            }
        });

    }

    public void saveSaleReturn(
            long invoiceId,
            JsonArray items,
            String refundMethod,
            String remarks,
            long returnedBy,
            SaleReturnCallback callback
    ) {

        JsonObject body = new JsonObject();

        body.addProperty("p_invoice_id", invoiceId);
        body.add("p_items", items);
        body.addProperty("p_refund_method", refundMethod);
        body.addProperty("p_remarks", remarks);
        body.addProperty("p_returned_by", returnedBy);

        apiService.saveSaleReturn(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {

                    callback.onSuccess();

                } else {

                    callback.onError("Return failed : " + response.code());

                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                callback.onError(t.getMessage());

            }
        });

    }


    public void searchInvoice(
            String invoiceNo,
            SearchInvoiceCallback callback
    ) {

        JsonObject body = new JsonObject();
        body.addProperty("p_invoice_no", invoiceNo);

        apiService.getInvoiceForReturn(body)
                .enqueue(new Callback<List<InvoiceReturnModel>>() {

                    @Override
                    public void onResponse(
                            Call<List<InvoiceReturnModel>> call,
                            Response<List<InvoiceReturnModel>> response) {

                        if (!response.isSuccessful()
                                || response.body() == null
                                || response.body().isEmpty()) {

                            callback.onError("চালানটি পাওয়া যায়নি!");
                            return;
                        }

                        InvoiceReturnModel invoice = response.body().get(0);

                        JsonObject itemBody = new JsonObject();
                        itemBody.addProperty("p_invoice_id", invoice.getId());

                        apiService.getInvoiceReturnItems(itemBody)
                                .enqueue(new Callback<List<SaleReturnItemModel>>() {

                                    @Override
                                    public void onResponse(
                                            Call<List<SaleReturnItemModel>> call,
                                            Response<List<SaleReturnItemModel>> response) {

                                        if (response.isSuccessful()
                                                && response.body() != null) {

                                            callback.onSuccess(invoice, response.body());

                                        } else {

                                            callback.onError("No products found");
                                        }
                                    }

                                    @Override
                                    public void onFailure(
                                            Call<List<SaleReturnItemModel>> call,
                                            Throwable t) {

                                        callback.onError(t.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onFailure(
                            Call<List<InvoiceReturnModel>> call,
                            Throwable t) {

                        callback.onError(t.getMessage());
                    }
                });

    }
}