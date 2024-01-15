package com.example.connectmeapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.connectmeapp.model.ApiService;
import com.example.connectmeapp.model.EncryptionUtils;
import com.example.connectmeapp.services.MyApiClient;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {
    private final ApiService apiService;
    private MutableLiveData<Boolean> authenticationResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AuthViewModel() {
        apiService = MyApiClient.getApiClient().create(ApiService.class);
    }

    public void authenticate(String email, String password) {
        if (init()) {
            System.out.println("Let's go!");
            return;
        }

        Call<Map<String, Boolean>> call = apiService.auth(EncryptionUtils.encrypt(email, password));
        call.enqueue(new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call,
                                   Response<Map<String, Boolean>> response) {
                if (response.isSuccessful()) {
                    Map<String, Boolean> result = response.body();

                    if (result != null
                            && result.get("result") != null
                            && Boolean.TRUE.equals(result.get("result"))) {

                        authenticationResult.setValue(true);
                    } else {
                        authenticationResult.setValue(false);
                    }
                } else {
                    authenticationResult.setValue(true);
                    errorMessage.setValue("Try again");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                errorMessage.setValue("Trouble with connection. Try again.");
            }
        });
    }

    private boolean init() {
        System.out.println("oisdssmvsvlkmfvlkmsvlkmsfkvmkds");
        AtomicBoolean success = new AtomicBoolean();
        Call<Map<String, Boolean>> call = apiService.init();
        call.enqueue(new Callback<Map<String, Boolean>>() {

            @Override
            public void onResponse(Call<Map<String, Boolean>> call,
                                   Response<Map<String, Boolean>> response) {
                if (response.isSuccessful()) {
                    Map<String, Boolean> result = response.body();

                    if (result != null && result.get("result") != null
                            && Boolean.TRUE.equals(result.get("result"))) {
                        success.set(true);
                    } else {
                        success.set(false);
                    }

                } else {
                    success.set(false);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                success.set(false);
            }
        });

        return success.get();
    }

    public LiveData<Boolean> getAuthenticationResult() {
        return authenticationResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}