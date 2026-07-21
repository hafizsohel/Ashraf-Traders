package com.example.ashraftraders.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.repository.CartRepository;
import com.example.ashraftraders.data.room.entity.CartEntity;

import java.util.List;

public class CartViewModel extends ViewModel {

    private final CartRepository repository;


    public CartViewModel(CartRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<CartEntity>> getCartItems() {
        return repository.getCartItems();
    }

    public LiveData<Integer> getCartCount() {
        return repository.getCartCount();
    }

    public LiveData<Double> getGrandTotal() {
        return repository.getGrandTotal();
    }

    public void addToCart(CartEntity item) {
        repository.addToCart(item);
    }

    public void increase(CartEntity item) {
        repository.increaseQuantity(item);
    }

    public void decrease(CartEntity item) {
        repository.decreaseQuantity(item);
    }

    public void remove(int id) {
        repository.removeItem(id);
    }
    public void clearCart() {
        repository.clearCart();
    }
}