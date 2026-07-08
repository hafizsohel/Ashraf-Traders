package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.ashraftraders.data.room.ProductDao;

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
        return productDao.getTotalPurchaseAmount();
    }

    public LiveData<Integer> getLowStockCount() {
        return productDao.getLowStockCount();
    }
}