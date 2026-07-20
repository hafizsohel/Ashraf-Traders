package com.example.ashraftraders.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ashraftraders.data.repository.CollectDueRepository;

public class CollectDueViewModel extends ViewModel {

    private final CollectDueRepository repository;

    private final MutableLiveData<Boolean> paymentResult = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public CollectDueViewModel(CollectDueRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> getPaymentResult() {
        return paymentResult;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void collectDuePayment(long invoiceId,
                                  double receiveAmount,
                                  String note,
                                  long receivedBy) {

        repository.collectDuePayment(
                invoiceId,
                receiveAmount,
                note,
                receivedBy,
                loading,
                paymentResult,
                message
        );
    }
}