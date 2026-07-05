package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.DashboardModel;
import com.example.ashraftraders.data.repository.DashboardRepository;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private final DashboardRepository repository;

    private final MutableLiveData<List<DashboardModel>> dashboardData =
            new MutableLiveData<>();

    public DashboardViewModel() {

        repository = new DashboardRepository();

    }

    public LiveData<List<DashboardModel>> getDashboardData() {

        return dashboardData;

    }

    public void loadDashboardStats() {

        repository.getDashboardSummary(dashboardData);

    }

}