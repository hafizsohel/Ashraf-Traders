package com.example.ashraftraders.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.data.repository.CartRepository;

public class CartViewModelFactory implements ViewModelProvider.Factory {

    private final CartRepository repository;

    public CartViewModelFactory(CartRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}