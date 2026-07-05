package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.ProductModel;
import com.example.ashraftraders.data.repository.ProductRepository;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private final ProductRepository repository;

    private final MutableLiveData<List<ProductModel>> productList =
            new MutableLiveData<>();

    public ProductViewModel() {
        repository = new ProductRepository();
    }

    public LiveData<List<ProductModel>> getProducts() {
        return productList;
    }

    public void loadProducts() {
        repository.getProducts(productList);
    }

    public void refreshProducts() {
        repository.getProducts(productList);
    }
}