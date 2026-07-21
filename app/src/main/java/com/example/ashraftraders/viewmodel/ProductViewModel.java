package com.example.ashraftraders.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/*

public class ProductViewModel extends ViewModel {

    private final ProductRepository repository;

    private final List<ProductModel> originalProducts = new ArrayList<>();
    private final MutableLiveData<List<ProductModel>> productList = new MutableLiveData<>();

    public ProductViewModel() {
        repository = new ProductRepository();
    }

    public LiveData<List<ProductModel>> getProducts() {
        return productList;
    }

    public void searchProducts(String keyword) {

        if (keyword == null) keyword = "";

        keyword = keyword.trim().toLowerCase();

        if (keyword.isEmpty()) {
            productList.setValue(new ArrayList<>(originalProducts));
            return;

        }

        List<ProductModel> filtered = new ArrayList<>();

        for (ProductModel product : originalProducts) {

            if (product.getProductName() != null &&
                    product.getProductName().toLowerCase().contains(keyword)) {

                filtered.add(product);

            }

        }

        productList.setValue(filtered);

    }
    public void loadProducts() {

        repository.getProducts(new ProductRepository.OnProductsLoadedListener() {

            @Override
            public void onSuccess(List<ProductModel> products) {

                originalProducts.clear();
                originalProducts.addAll(products);

                productList.setValue(products);

            }

            @Override
            public void onError(String message) {

                // Handle error if needed

            }

        });

    }
    public void filterProducts(String brand,
                               String stock,
                               String sort){

        List<ProductModel> filtered=new ArrayList<>(originalProducts);

        // Brand Filter

        if(!brand.equals("All")){

            Iterator<ProductModel> iterator=filtered.iterator();

            while(iterator.hasNext()){

                ProductModel p=iterator.next();

                if(!p.getBrandName().equalsIgnoreCase(brand)){

                    iterator.remove();

                }

            }

        }

        // পরে Stock Filter

        // পরে Sort

        productList.setValue(filtered);

    }
}*/


import androidx.lifecycle.MediatorLiveData;

public class ProductViewModel extends ViewModel {

    private final ProductRepository repository;

    // মেইন ডেটাবেজ থেকে আসা অরিজিনাল লিস্ট ক্যাশ রাখার জন্য
    private final List<ProductModel> originalProducts = new ArrayList<>();

    // ফিল্টার এবং সার্চ অ্যাপ্লাই করা ফাইনাল লিস্ট যা UI অবজার্ভ করবে
    private final MediatorLiveData<List<ProductModel>> productList = new MediatorLiveData<>();

    // নেটওয়ার্ক সিঙ্ক বা লোডিং স্টেট ট্র্যাক করার জন্য (অপশনাল কিন্তু দরকারি)
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    // বর্তমান ফিল্টার স্টেটসমূহ ট্র্যাকিং ভ্যারিয়েবল
    private String currentKeyword = "";
    private String currentBrand = "All";
    private String currentStock = "All";
    private String currentSort = "Name A-Z";

    // কনস্ট্রাক্টরে অফলাইন-রেডি রিপোজিটরি ইনজেক্ট করা হয়েছে
    public ProductViewModel(ProductRepository repository) {
        this.repository = repository;

        // ডেটাবেজের লাইভ-ডেটা সোর্সের সাথে সিঙ্ক করা (সবচেয়ে নিরাপদ ও আধুনিক নিয়ম)
        productList.addSource(repository.getAllProducts(), products -> {
            Log.d("ROOM", "Products = " + (products == null ? 0 : products.size()));
            originalProducts.clear();
            if (products != null) {
                originalProducts.addAll(products);
            }
            // লোকাল ডেটাবেজে কোনো পরিবর্তন আসলেই কারেন্ট ফিল্টার ও সার্চ বজায় রেখে UI রিফ্রেশ হবে
            applyFilterAndSearch();
        });
    }

    // UI-তে অবজার্ভ করার জন্য এক্সপোজড মেথডসমূহ
    public LiveData<List<ProductModel>> getProducts() {
        return productList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // ==========================================
    // ১. অনলাইন থেকে ডেটা এনে অফলাইনে সিঙ্ক করা
    // ==========================================
    public interface AddProductListener {
        void onSuccess();

        void onError(String message);
    }

    public void addProductToServerAndRoom(ProductModel product) {

        isLoading.setValue(true);

        repository.addProduct(product, new ProductRepository.OnSyncListener() {

            @Override
            public void onSyncSuccess() {

                repository.fetchAndSyncProducts(new ProductRepository.OnSyncListener() {

                    @Override
                    public void onSyncSuccess() {
                        isLoading.postValue(false);
                    }

                    @Override
                    public void onSyncError(String message) {
                        isLoading.postValue(false);

                    }
                });
            }

            @Override
            public void onSyncError(String message) {

                isLoading.postValue(false);

            }
        });
    }

    // ==========================================
    // ২. লোকাল ডেটাবেজ ক্রুড (CRUD) অপারেশনস
    // ==========================================
    public void insertProduct(ProductModel product) {
        repository.insert(product);
    }

    public void updateProduct(ProductModel product) {
        repository.update(product);
    }

    public void deleteProduct(ProductModel product) {
        repository.delete(product);
    }

    public void deleteAllProducts() {
        repository.deleteAll();
    }

    // ==========================================
    // ৩. সার্চ এবং ফিল্টারিং মেকানিজম (ইন-মেমোরি)
    // ==========================================

    // শুধু সার্চ টেক্সট আপডেট করবে
    public void searchProducts(String keyword) {
        this.currentKeyword = keyword != null ? keyword.trim().toLowerCase() : "";
        applyFilterAndSearch();
    }

    // ফিল্টার এবং সর্টিং কন্ডিশন আপডেট করবে
    public void filterProducts(String brand, String stock, String sort) {
        this.currentBrand = brand != null ? brand : "All";
        this.currentStock = stock != null ? stock : "All";
        this.currentSort = sort != null ? sort : "Name A-Z";
        applyFilterAndSearch();
    }

    // মূল লজিক যা সার্চ, ব্র্যান্ড, স্টক ও সর্টিং একসাথে প্রসেস করে মেইন থ্রেডে পুশ করে
    private void applyFilterAndSearch() {
        List<ProductModel> filtered = new ArrayList<>(originalProducts);

        // ১. সার্চ কুয়েরি ফিল্টার (পণ্যের নাম, কোড বা ব্র্যান্ড এর সাথে ম্যাচিং)
        if (!currentKeyword.isEmpty()) {
            Iterator<ProductModel> iterator = filtered.iterator();
            while (iterator.hasNext()) {
                ProductModel p = iterator.next();
                boolean match = (p.getProductName() != null && p.getProductName().toLowerCase().contains(currentKeyword))
                        || (p.getProduct_code() != null && p.getProduct_code().toLowerCase().contains(currentKeyword))
                        || (p.getBrandName() != null && p.getBrandName().toLowerCase().contains(currentKeyword));
                if (!match) {
                    iterator.remove();
                }
            }
        }

        // ২. ব্র্যান্ড ফিল্টার
        if (!currentBrand.equalsIgnoreCase("All")) {
            Iterator<ProductModel> iterator = filtered.iterator();
            while (iterator.hasNext()) {
                ProductModel p = iterator.next();
                if (p.getBrandName() == null || !p.getBrandName().equalsIgnoreCase(currentBrand)) {
                    iterator.remove();
                }
            }
        }

        // ৩. স্টক ফিল্টার
        if (!currentStock.equalsIgnoreCase("All")) {
            Iterator<ProductModel> iterator = filtered.iterator();
            while (iterator.hasNext()) {
                ProductModel p = iterator.next();
                switch (currentStock) {
                    case "In Stock":
                        if (p.getStock() <= 0) iterator.remove();
                        break;
                    case "Out of Stock":
                        if (p.getStock() > 0) iterator.remove();
                        break;
                    case "Low Stock":
                        if (p.getStock() > 10 || p.getStock() <= 0) iterator.remove();
                        break;
                }
            }
        }

        // ৪. সর্টিং (Sorting) বা সাজানো
        switch (currentSort) {
            case "Name A-Z":
                filtered.sort((a, b) -> {
                    if (a.getProductName() == null) return 1;
                    if (b.getProductName() == null) return -1;
                    return a.getProductName().compareToIgnoreCase(b.getProductName());
                });
                break;

            case "Name Z-A":
                filtered.sort((a, b) -> {
                    if (a.getProductName() == null) return -1;
                    if (b.getProductName() == null) return 1;
                    return b.getProductName().compareToIgnoreCase(a.getProductName());
                });
                break;

            case "Price Low-High":
                filtered.sort((a, b) -> Double.compare(a.getPurchasePrice(), b.getSalePrice()));
                break;

            case "Price High-Low":
                filtered.sort((a, b) -> Double.compare(b.getPurchasePrice(), a.getSalePrice()));
                break;
        }

        // লাইভ ডেটার ভ্যালু আপডেট করা
        productList.setValue(filtered);
    }

    public void syncProducts() {

        isLoading.setValue(true);

        repository.fetchAndSyncProducts(new ProductRepository.OnSyncListener() {
            @Override
            public void onSyncSuccess() {
                isLoading.postValue(false);
            }

            @Override
            public void onSyncError(String message) {
                isLoading.postValue(false);
            }
        });
    }

}