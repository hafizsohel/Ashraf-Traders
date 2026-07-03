package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ashraftraders.data.model.User;
import com.example.ashraftraders.data.repository.LoginRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final LoginRepository repository = new LoginRepository();

    private final MutableLiveData<User> loginSuccess = new MutableLiveData<>();

    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<User> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getError() {
        return error;
    }
    public String validateInput(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            return "ইউজার নাম লিখুন";
        }

        if (password == null || password.trim().isEmpty()) {
            return "পাসওয়ার্ড লিখুন";
        }

        return null;
    }

    public void login(String username, String password) {

        String validation = validateInput(username, password);

        if (validation != null) {
            error.setValue(validation);
            return;
        }

        repository.login(username, password)
                .enqueue(new Callback<List<User>>() {

                    @Override
                    public void onResponse(Call<List<User>> call,
                                           Response<List<User>> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && !response.body().isEmpty()) {

                            loginSuccess.postValue(response.body().get(0));

                        } else {

                            error.postValue("ইউজার নাম অথবা পাসওয়ার্ড সঠিক নয়!");

                        }

                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {

                        error.postValue(t.getMessage());

                    }
                });
    }

}