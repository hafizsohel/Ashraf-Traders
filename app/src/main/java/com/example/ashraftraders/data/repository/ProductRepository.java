package com.example.ashraftraders.data.repository;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.room.AppDatabase;
import com.example.ashraftraders.data.room.ProductDao;
import com.example.ashraftraders.data.room.ProductEntity;
import com.example.ashraftraders.data.room.ProductMapper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private static final String TAG = "ProductRepository";
    private final ApiService apiService;
    private final ProductDao productDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public LiveData<List<ProductModel>> getAllProducts() {
        return Transformations.map(
                productDao.getAllProducts(),
                ProductMapper::toModelList
        );
    }

    public void insert(ProductModel model) {
        executor.execute(() -> productDao.insert(ProductMapper.toEntity(model)));
    }

    public void update(ProductModel model) {
        executor.execute(() -> productDao.update(ProductMapper.toEntity(model)));
    }

    public void delete(ProductModel model) {
        executor.execute(() -> productDao.delete(ProductMapper.toEntity(model)));
    }

    public void deleteAll() {
        executor.execute(productDao::deleteAll);
    }

    public interface OnSyncListener {
        void onSyncSuccess();
        void onSyncError(String message);
    }

    public void fetchAndSyncProducts(OnSyncListener listener) {

        apiService.getProductList().enqueue(new Callback<List<ProductModel>>() {

            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call,
                                   @NonNull Response<List<ProductModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<ProductModel> remoteProducts = response.body();

                    executor.execute(() -> {

                        try {
                            productDao.deleteAll();
                            productDao.insertAll(ProductMapper.toEntityList(remoteProducts));

                            // Insert শেষ হওয়ার পর callback
                            if (listener != null) {
                                listener.onSyncSuccess();
                            }

                        } catch (Exception e) {

                            Log.e(TAG, "Database Error", e);

                            if (listener != null) {
                                listener.onSyncError(e.getMessage());
                            }
                        }

                    });

                } else {

                    if (listener != null) {
                        listener.onSyncError("Server Error : " + response.code());
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call,
                                  @NonNull Throwable t) {

                if (listener != null) {
                    listener.onSyncError(t.getMessage());
                }

            }
        });

    }

    public void addProduct(ProductModel product, OnSyncListener listener) {
        // ১. প্রথমে Retrofit API এর মাধ্যমে সার্ভারে ডাটা পাঠানো হচ্ছে
        // নোট: আপনার ApiService-এaddProduct(product) নামক একটি POST মেথড থাকতে হবে

        String myAnonKey = "sb_publishable_8GLmErLipG89oDKeIcEpcw_wnTuSvv_";
        String bearerToken = "Bearer " + myAnonKey;
        apiService.addProduct(product).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProductModel> call,
                                   @NonNull retrofit2.Response<ProductModel> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ProductModel savedProduct = response.body();

                    executor.execute(() -> {
                        try {
                            // লোকাল রুমে সেভ করা হচ্ছে
                            productDao.insert(ProductMapper.toEntity(savedProduct));

                            if (listener != null) {
                                listener.onSyncSuccess();
                            }
                            Log.e("SUPABASE", response.errorBody().string());
                            if (listener != null) listener.onSyncSuccess();
                        } catch (Exception e) {
                            if (listener != null) listener.onSyncError(e.getMessage());
                        }
                    });

                } else {
                    if (listener != null) listener.onSyncError("Server Error Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ProductModel> call, @NonNull Throwable t) {
                if (listener != null) listener.onSyncError(t.getMessage());
            }
        });
    }
}