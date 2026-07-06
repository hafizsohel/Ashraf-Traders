package com.example.ashraftraders.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.ashraftraders.data.room.ProductDao;
import com.example.ashraftraders.viewmodel.DashboardViewModel;

public class DashboardViewModelFactory implements ViewModelProvider.Factory {

    private final ProductDao productDao;

    // ড্যাশবোর্ডের জন্য ProductDao কনস্ট্রাক্টরে নেওয়া হচ্ছে
    public DashboardViewModelFactory(ProductDao productDao) {
        this.productDao = productDao;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // এই ফ্যাক্টরি শুধুমাত্র DashboardViewModel তৈরি করতে পারবে
        if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            return (T) new DashboardViewModel(productDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class. This factory is only for DashboardViewModel.");
    }
}