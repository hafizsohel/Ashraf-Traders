package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.example.ashraftraders.data.room.ProductDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardViewModel extends ViewModel {

    private final ProductDao productDao;

    // কনস্ট্রাক্টরে ProductDao ইনজেক্ট করা হয়েছে অফলাইন ডেটার জন্য
    public DashboardViewModel(ProductDao productDao) {
        this.productDao = productDao;
    }

    public LiveData<Integer> getTotalProducts() {
        return productDao.getTotalProductsCount();
    }

    public LiveData<Integer> getTotalBrands() {
        return productDao.getTotalBrandsCount();
    }

    public LiveData<Double> getTotalPurchase() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        return Transformations.map(productDao.getTodayPurchaseAmount(today), total -> {
            // যদি ডাটাবেজ null ফেরত দেয়, তবে 0.0 পাঠাবে
            return (total != null) ? total : 0.0;
        });
    }

    public LiveData<Integer> getLowStockCount() {
        return productDao.getLowStockCount();
    }
}