package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.DueModel;
import com.example.ashraftraders.data.repository.DueRepository;

import java.util.List;

public class DueViewModel extends ViewModel {

    private final DueRepository repository;

    public MutableLiveData<List<DueModel>> dueList = new MutableLiveData<>();

    public DueViewModel(DueRepository repository) {
        this.repository = repository;
    }

    public void loadDueList() {
        repository.getDueInvoiceList(dueList);
    }

}