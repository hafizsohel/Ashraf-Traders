package com.example.ashraftraders.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ashraftraders.data.repository.CollectDueRepository;

public class CollectDueViewModelFactory implements ViewModelProvider.Factory {

    private final CollectDueRepository repository;

    public CollectDueViewModelFactory(CollectDueRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(CollectDueViewModel.class)) {
            return (T) new CollectDueViewModel(repository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}