package com.example.ashraftraders.views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.ashraftraders.R;
import com.example.ashraftraders.data.api.ApiClient;
import com.example.ashraftraders.data.api.ApiService;
import com.example.ashraftraders.data.model.BrandModel;
import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.room.AppDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductBottomSheet extends BottomSheetDialogFragment {
    private List<BrandModel> brandList = new ArrayList<>();
    private long selectedBrandId = -1;

    public interface OnProductAddListener {
        void onProductSubmit(ProductModel product);
    }

    private final OnProductAddListener listener;

    public AddProductBottomSheet(OnProductAddListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialAutoCompleteTextView etName = view.findViewById(R.id.etProductName);
        TextInputEditText etPrice = view.findViewById(R.id.etPurchasePrice);
        MaterialAutoCompleteTextView etBrandName = view.findViewById(R.id.etBrandName);        TextInputEditText etStock = view.findViewById(R.id.etStock);
        MaterialButton btnSubmit = view.findViewById(R.id.btnSubmitProduct);
        loadBrands(etBrandName);

        etBrandName.setOnItemClickListener((parent, view1, position, id) -> {
            selectedBrandId = brandList.get(position).getId();
        });

        // রুম ডেটাবেজ থেকে পণ্যের নাম এনে ড্রপডাউনে সেট করা
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                List<String> productNames = db.productDao().getProductNamesSync();

                if (productNames == null) {
                    productNames = new ArrayList<>();
                }

                List<String> finalProductNames = productNames;
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                finalProductNames
                        );
                        etName.setAdapter(adapter);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // সাবমিট বাটন ক্লিক লজিক
        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String brandStr = etBrandName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String stockStr = etStock.getText().toString().trim();
            if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(requireContext(), "সবগুলো ফিল্ড পূরণ করুন", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedBrandId == -1) {
                Toast.makeText(requireContext(), "ব্র্যান্ড নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isNetworkAvailable()) {
                Toast.makeText(requireContext(), "ইন্টারনেট কানেকশন নেই! অফলাইনে নতুন পণ্য যোগ করা সম্ভব নয়।", Toast.LENGTH_SHORT).show();
                return;
            }

            // মডেল অবজেক্ট তৈরি (বাকি সব ফিল্ড অটোমেটিক null থাকবে)
            ProductModel product = new ProductModel();
            product.setProductName(name);
            product.setBrandId(selectedBrandId);
            product.setPurchasePrice(Double.parseDouble(priceStr));
            product.setStock(Integer.parseInt(stockStr));

            if (listener != null) {
                listener.onProductSubmit(product);
            }
            dismiss();
        });
    }


    // ইন্টারনেট চেক করার নিজস্ব মেথড
    private boolean isNetworkAvailable() {
        Context context = getContext();
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void loadBrands(MaterialAutoCompleteTextView etBrandName) {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getBrands().enqueue(new Callback<List<BrandModel>>() {

            @Override
            public void onResponse(Call<List<BrandModel>> call,
                                   Response<List<BrandModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    brandList = response.body();

                    List<String> names = new ArrayList<>();

                    for (BrandModel brand : brandList) {
                        names.add(brand.getBrandName());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(
                                    requireContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    names);

                    etBrandName.setAdapter(adapter);

                } else {

                    Toast.makeText(requireContext(),
                            "Brand Load Failed",
                            Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<BrandModel>> call,
                                  Throwable t) {

                Toast.makeText(requireContext(),
                        t.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}