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
    private final MutableLiveData<Boolean> createAccResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void authenticate(ApiService apiService, String email) {
        Call<Map<String, Boolean>> call = apiService.auth(email);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Boolean>> call,
                                   @NonNull Response<Map<String, Boolean>> response) {

                if (response.isSuccessful()) {
                    Map<String, Boolean> result = response.body();
                    if (result != null && Boolean.TRUE.equals(result.get(email))) {
                        authResult.setValue(true);
                    } else {
                        authResult.setValue(false);
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
        Call<Map<String, Boolean>> call = apiService.init(cryptoAuth);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Boolean>> call,
                                   @NonNull Response<Map<String, Boolean>> response) {

                if (response.isSuccessful()) {
                    Map<String, Boolean> result = response.body();
                    if (result == null) return;

                    initResult.setValue(!Objects.equals(result.get("result"), "false"));
                } else {
                    initResult.setValue(false);
                    errorMessage.setValue("Try again");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Boolean>> call,
                                  @NonNull Throwable t) {
                initResult.setValue(false);
                errorMessage.setValue("Trouble with init. Try again.");
            }
        });
    }

    public void createAccount(ApiService api, String email, String userName) {
        Call<Map<String,Boolean>> call = api.createNewAccount(email, userName);
        call.enqueue(new  Callback<>(){

            @Override
            public void onResponse(@NonNull Call<Map<String, Boolean>> call,
                                   @NonNull Response<Map<String, Boolean>> response) {
                if (response.isSuccessful()){
                    Map<String,Boolean> result = response.body();
                    createAccResult.setValue(Objects.requireNonNull(result).get("result"));
                } else {
                    createAccResult.setValue(false);
                }

            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                createAccResult.setValue(false);
            }
        });
    }

    public LiveData<Boolean> getInitResult() {
        return initResult;
    }

    public LiveData<Boolean> getAuthResult() {
        return authResult;
    }

    public MutableLiveData<Boolean> getCreateAccResult() {
        return createAccResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}