package com.example.ashraftraders.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.ashraftraders.data.repository.SalesRepository;

public class SalesViewModelFactory implements ViewModelProvider.Factory {

    private final SalesRepository repository;

    public SalesViewModelFactory(SalesRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new SalesViewModel(repository);
    }
}