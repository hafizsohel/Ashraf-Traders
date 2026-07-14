package com.example.ashraftraders.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.data.repository.CustomerRepository;

public class CustomerViewModelFactory implements ViewModelProvider.Factory {

    private final CustomerRepository repository;

    public CustomerViewModelFactory(CustomerRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(CustomerViewModel.class)) {
            return (T) new CustomerViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel");
    }
}