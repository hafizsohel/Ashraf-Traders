package com.example.ashraftraders.data.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.invoice.InvoiceRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceRepository {

    private final ApiService apiService;

    public InvoiceRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public interface OnInvoiceListener {
        void onSuccess(Long invoiceId);

        void onError(String message);
    }

    public void saveInvoice(InvoiceRequest request,
                            OnInvoiceListener listener) {

        apiService.saveInvoice(request).enqueue(new Callback<Long>() {

            @Override
            public void onResponse(@NonNull Call<Long> call,
                                   @NonNull Response<Long> response) {

                if (response.isSuccessful() && response.body() != null) {

                    listener.onSuccess(response.body());

                } else {

                    String error = "";

                    try {

                        if (response.errorBody() != null)
                            error = response.errorBody().string();

                    } catch (Exception e) {
                        error = e.getMessage();
                    }
                    Log.e("INVOICE_SAVE", "FAILED");
                    Log.e("INVOICE_SAVE", "HTTP CODE = " + response.code());
                    Log.e("INVOICE_SAVE", "ERROR BODY = " + error);

                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Long> call,
                                  @NonNull Throwable t) {

                listener.onError(t.getMessage());
            }
        });

    }
}
 /*   public void saveInvoice(InvoiceRequest request,
                            OnInvoiceListener listener) {

        apiService.saveInvoice(request).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {

                Log.d("INVOICE_SAVE", "URL = " + call.request().url());
                Log.d("INVOICE_SAVE", "HTTP CODE = " + response.code());

                if (response.isSuccessful()) {

                    Log.d("INVOICE_SAVE", "SUCCESS");

                    listener.onSuccess();

                } else {

                    String errorBody = "";

                    try {

                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }

                    } catch (Exception e) {
                        errorBody = e.getMessage();
                    }

                    Log.e("INVOICE_SAVE", "FAILED");
                    Log.e("INVOICE_SAVE", "HTTP CODE = " + response.code());
                    Log.e("INVOICE_SAVE", "ERROR BODY = " + errorBody);

                    listener.onError(errorBody);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,
                                  @NonNull Throwable t) {

                Log.e("INVOICE_SAVE", "NETWORK FAILURE", t);

                listener.onError(t.getMessage());
            }
        });
    }*/