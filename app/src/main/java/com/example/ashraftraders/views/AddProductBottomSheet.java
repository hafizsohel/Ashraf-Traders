package com.example.ashraftraders.views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.ashraftraders.R;
import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.room.AppDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddProductBottomSheet extends BottomSheetDialogFragment {

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
        TextInputEditText etStock = view.findViewById(R.id.etStock);
        MaterialButton btnSubmit = view.findViewById(R.id.btnSubmitProduct);

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
            String priceStr = etPrice.getText().toString().trim();
            String stockStr = etStock.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(requireContext(), "সবগুলো ফিল্ড পূরণ করুন", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isNetworkAvailable()) {
                Toast.makeText(requireContext(), "ইন্টারনেট কানেকশন নেই! অফলাইনে নতুন পণ্য যোগ করা সম্ভব নয়।", Toast.LENGTH_SHORT).show();
                return;
            }

            // মডেল অবজেক্ট তৈরি (বাকি সব ফিল্ড অটোমেটিক null থাকবে)
            ProductModel product = new ProductModel();
            product.setProductName(name);
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
}