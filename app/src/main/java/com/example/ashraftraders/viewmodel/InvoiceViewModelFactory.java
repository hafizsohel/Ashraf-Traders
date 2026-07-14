package com.example.ashraftraders.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.data.repository.InvoiceRepository;

public class InvoiceViewModelFactory implements ViewModelProvider.Factory {

    private final InvoiceRepository repository;

    public InvoiceViewModelFactory(InvoiceRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(InvoiceViewModel.class)) {
            return (T) new InvoiceViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}