package com.example.ashraftraders.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.data.repository.DueRepository;

public class DueViewModelFactory implements ViewModelProvider.Factory {

    private final DueRepository repository;

    public DueViewModelFactory(DueRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(DueViewModel.class)) {
            return (T) new DueViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}