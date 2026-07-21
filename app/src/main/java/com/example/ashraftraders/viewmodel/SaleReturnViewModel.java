package com.example.ashraftraders.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.InvoiceReturnModel;
import com.example.ashraftraders.data.model.SaleReturnItemModel;
import com.example.ashraftraders.data.repository.SalesRepository;
import com.google.gson.JsonArray;

import java.util.List;

public class SaleReturnViewModel extends ViewModel {

    private final SalesRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> success = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<InvoiceReturnModel> invoice =
            new MutableLiveData<>();

    private final MutableLiveData<List<SaleReturnItemModel>> items =
            new MutableLiveData<>();

    public LiveData<InvoiceReturnModel> getInvoice() {
        return invoice;
    }

    public LiveData<List<SaleReturnItemModel>> getItems() {
        return items;
    }

    public SaleReturnViewModel() {
        repository = new SalesRepository();
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getSuccess() {
        return success;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void saveSaleReturn(
            long invoiceId,
            JsonArray items,
            String refundMethod,
            String remarks,
            long returnedBy
    ) {

        loading.setValue(true);

        repository.saveSaleReturn(
                invoiceId,
                items,
                refundMethod,
                remarks,
                returnedBy,
                new SalesRepository.SaleReturnCallback() {

                    @Override
                    public void onSuccess() {

                        loading.postValue(false);
                        success.postValue(true);

                    }

                    @Override
                    public void onError(String message) {

                        loading.postValue(false);
                        error.postValue(message);

                    }
                }
        );
    }

    public void searchInvoice(String invoiceNo) {

        loading.setValue(true);

        repository.searchInvoice(
                invoiceNo,
                new SalesRepository.SearchInvoiceCallback() {

                    @Override
                    public void onSuccess(
                            InvoiceReturnModel invoiceModel,
                            List<SaleReturnItemModel> list
                    ) {

                        loading.postValue(false);

                        invoice.postValue(invoiceModel);

                        items.postValue(list);

                    }

                    @Override
                    public void onError(String message) {

                        loading.postValue(false);

                        error.postValue(message);

                    }
                });

    }
}