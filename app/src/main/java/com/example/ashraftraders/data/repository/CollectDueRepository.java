package com.example.ashraftraders.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectDueRepository {

    private final ApiService apiService;

    public CollectDueRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void collectDuePayment(
            long invoiceId,
            double receiveAmount,
            String note,
            long receivedBy,
            MutableLiveData<Boolean> loading,
            MutableLiveData<Boolean> result,
            MutableLiveData<String> message
    ) {

        JsonObject body = new JsonObject();

        body.addProperty("p_invoice_id", invoiceId);
        body.addProperty("p_receive_amount", receiveAmount);
        body.addProperty("p_note", note);
        body.addProperty("p_received_by", receivedBy);


        Log.d("CollectDue", "========== REQUEST ==========");
        Log.d("CollectDue", "Invoice ID : " + invoiceId);
        Log.d("CollectDue", "Receive Amount : " + receiveAmount);
        Log.d("CollectDue", "Note : " + note);
        Log.d("CollectDue", "Received By : " + receivedBy);
        Log.d("CollectDue", "Body : " + body.toString());

        apiService.collectDuePayment(body).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.d("CollectDue", "========== RESPONSE ==========");
                Log.d("CollectDue", "HTTP Code : " + response.code());
                Log.d("CollectDue", "Successful : " + response.isSuccessful());

                if (response.errorBody() != null) {
                    try {
                        Log.e("CollectDue", response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (response.isSuccessful()) {
                    Log.d("CollectDue", "Payment Success -> Reload list");
                    result.postValue(true);
                    message.postValue("বকেয়া সফলভাবে আদায় হয়েছে");

                } else {

                    result.postValue(false);
                    message.postValue("Payment Failed");

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                result.postValue(false);

                if (t.getMessage() == null) {
                    message.postValue("Unknown Error");
                } else {
                    message.postValue(t.getMessage());
                }
            }

        });

    }

}