package com.example.ashraftraders.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.customer.CustomerModel;
import com.example.ashraftraders.data.model.customer.CustomerRequest;
import com.example.ashraftraders.data.repository.CustomerRepository;

public class CustomerViewModel extends ViewModel {

    private final CustomerRepository repository;

    private final MutableLiveData<CustomerModel> customer = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<CustomerModel> savedCustomer = new MutableLiveData<>();

    public LiveData<CustomerModel> getSavedCustomer() {
        return savedCustomer;
    }

    public CustomerViewModel(CustomerRepository repository) {
        this.repository = repository;
    }

    public LiveData<CustomerModel> getCustomer() {
        return customer;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void searchCustomer(String phone) {

        repository.searchCustomer(phone, new CustomerRepository.SearchListener() {

            @Override
            public void onSuccess(CustomerModel model) {
                customer.postValue(model);
            }

            @Override
            public void onNotFound() {
                customer.postValue(null);
            }

            @Override
            public void onError(String message) {
                error.postValue(message);
            }
        });
    }

    public void saveCustomer(CustomerRequest request) {

        repository.saveCustomer(request, new CustomerRepository.SaveListener() {

            @Override
            public void onSuccess(CustomerModel model) {
                customer.postValue(model);
            }

            @Override
            public void onError(String message) {
                error.postValue(message);
            }
        });
    }
}