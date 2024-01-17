package com.example.connectmeapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.connectmeapp.model.ApiService;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<Boolean> authResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> initResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private String key = "";

    public void authenticate(ApiService apiService, String email) {
        Call<Map<String, Boolean>> call = apiService.auth(email);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Boolean>> call,
                                   @NonNull Response<Map<String, Boolean>> response) {

                if (response.isSuccessful()) {
                    Map<String, Boolean> result = response.body();
                    if (result != null && result.get("result") != null
                            && !result.get("result").equals(false)) {

                        authResult.setValue(true);
                    } else {
                        errorMessage.setValue("Try again");
                    }

                } else {
                    authResult.setValue(false);
                    errorMessage.setValue("Try again");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Boolean>> call, @NonNull Throwable t) {
                authResult.setValue(false);
                errorMessage.setValue("Trouble with connection. Try again.");
            }
        });
    }

    public void init(ApiService apiService, String cryptoAuth) {
        Call<Map<String, String>> call = apiService.init(cryptoAuth);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call,
                                   @NonNull Response<Map<String, String>> response) {

                if (response.isSuccessful()) {
                    Map<String, String> result = response.body();
                    if (result == null) return;
                    
                    key = result.get("result");
                    initResult.setValue(!Objects.equals(result.get("result"), "false"));
                } else {
                    initResult.setValue(false);
                    errorMessage.setValue("Try again");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call,
                                  @NonNull Throwable t) {
                initResult.setValue(false);
                errorMessage.setValue("Trouble with connection. Try again.");
            }
        });
    }

    public LiveData<Boolean> getInitResult() {
        return initResult;
    }

    public LiveData<Boolean> getAuthResult() {
        return authResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}