package com.example.ashraftraders.data.repository;


import android.util.Log;

import androidx.annotation.NonNull;
import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.customer.CustomerModel;
import com.example.ashraftraders.data.model.customer.CustomerRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRepository {

    private final ApiService apiService;

    public CustomerRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public interface SearchListener {
        void onSuccess(CustomerModel customer);
        void onNotFound();
        void onError(String message);
    }

    public interface SaveListener {
        void onSuccess(CustomerModel customer);
        void onError(String message);
    }

    public void searchCustomer(String phone,
                               SearchListener listener) {

        Map<String, String> body = new HashMap<>();
        body.put("phone_input", phone);

        apiService.searchCustomer(body).enqueue(new Callback<List<CustomerModel>>() {

            @Override
            public void onResponse(@NonNull Call<List<CustomerModel>> call,
                                   @NonNull Response<List<CustomerModel>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && !response.body().isEmpty()) {
                    Log.d("CUSTOMER_SEARCH", "HTTP = " + response.code());

                    if (response.body() != null) {
                        Log.d("CUSTOMER_SEARCH", "Size = " + response.body().size());
                    }

                    listener.onSuccess(response.body().get(0));

                } else {

                    listener.onNotFound();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CustomerModel>> call,
                                  @NonNull Throwable t) {

                listener.onError(t.getMessage());
            }
        });
    }

    public void saveCustomer(CustomerRequest request,
                             SaveListener listener) {

        apiService.saveCustomer(request).enqueue(new Callback<CustomerModel>() {

           /* @Override
            public void onResponse(@NonNull Call<CustomerModel> call,
                                   @NonNull Response<CustomerModel> response) {

                if (response.isSuccessful() && response.body() != null) {

                    listener.onSuccess(response.body());

                } else {

                  //  listener.onError("Customer Save Failed");

                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("CUSTOMER_SAVE", "Success : " + response.body().getCustomerName());
                        listener.onSuccess(response.body());

                    } else {
                        try {
                            listener.onError(
                                    "Code : " + response.code() +
                                            "\nError : " + response.errorBody().string()
                            );
                            Log.d("CUSTOMER_SAVE", "Error : " + response.errorBody().string());

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerModel> call,
                                  @NonNull Throwable t) {

                listener.onError(t.getMessage());
            }*/
           @Override
           public void onResponse(@NonNull Call<CustomerModel> call,
                                  @NonNull Response<CustomerModel> response) {

               if (response.isSuccessful() && response.body() != null) {

                   Log.d("CUSTOMER_SAVE", "SUCCESS");
                   Log.d("CUSTOMER_SAVE", "Customer = " + response.body().getCustomerName());

                   listener.onSuccess(response.body());

               } else {

                   String errorBody = "";

                   try {

                       if (response.errorBody() != null) {
                           errorBody = response.errorBody().string();
                       }

                   } catch (Exception e) {
                       errorBody = e.getMessage();
                   }

                   Log.e("CUSTOMER_SAVE", "FAILED");
                   Log.e("CUSTOMER_SAVE", "HTTP CODE = " + response.code());
                   Log.e("CUSTOMER_SAVE", "ERROR BODY = " + errorBody);

                   listener.onError(errorBody);
               }
           }

            @Override
            public void onFailure(@NonNull Call<CustomerModel> call,
                                  @NonNull Throwable t) {

                Log.e("CUSTOMER_SAVE", "NETWORK FAILURE", t);

                listener.onError(t.getMessage());
            }
        });
    }
}