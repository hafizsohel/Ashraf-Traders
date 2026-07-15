package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.SalesSummaryModel;
import com.example.ashraftraders.data.repository.SalesRepository;

public class SalesViewModel extends ViewModel {

    private final SalesRepository repository;

    private final MutableLiveData<SalesSummaryModel> summary =
            new MutableLiveData<>();

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    public SalesViewModel(SalesRepository repository) {
        this.repository = repository;
    }

    public LiveData<SalesSummaryModel> getSummary() {
        return summary;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadSummary() {

        repository.getSalesSummary(new SalesRepository.SummaryListener() {

            @Override
            public void onSuccess(SalesSummaryModel model) {

                summary.postValue(model);

            }

            @Override
            public void onError(String message) {

                error.postValue(message);

            }
        });
    }
}