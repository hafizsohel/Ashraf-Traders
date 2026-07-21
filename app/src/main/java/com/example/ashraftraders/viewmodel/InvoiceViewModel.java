package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.invoice.InvoiceRequest;
import com.example.ashraftraders.data.repository.InvoiceRepository;

public class InvoiceViewModel extends ViewModel {

    private final InvoiceRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> success = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Long> invoiceId = new MutableLiveData<>();

    public LiveData<Long> getInvoiceId() {
        return invoiceId;
    }

    public InvoiceViewModel(InvoiceRepository repository) {
        this.repository = repository;
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

    public void saveInvoice(InvoiceRequest request) {

        loading.setValue(true);

        repository.saveInvoice(request, new InvoiceRepository.OnInvoiceListener() {

            @Override
            public void onSuccess(Long id) {

                loading.postValue(false);

                invoiceId.postValue(id);

                success.postValue(true);
            }

            @Override
            public void onError(String message) {

                loading.postValue(false);

                error.postValue(message);

                success.postValue(false);
            }
        });
    }
}