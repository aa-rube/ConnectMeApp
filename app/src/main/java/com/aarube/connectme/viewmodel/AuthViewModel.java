package com.aarube.connectme.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.aarube.connectme.model.ApiInitService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<Boolean> authResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> initResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> createAccResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> sessionId = new MutableLiveData<>();

    public void authenticate(ApiInitService apiInitService, String email) {
        Call<Map<String, Boolean>> call = apiInitService.auth(email);
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

    public void init(ApiInitService apiInitService, String cryptoAuth) {
        Call<Map<String, String>> call = apiInitService.init(cryptoAuth);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call,
                                   @NonNull Response<Map<String, String>> response) {

                if (response.isSuccessful()) {
                    Map<String, String> result = response.body();

                    if (result == null) return;
                    initResult.setValue(!result.get("result").equals("refuse"));
                    sessionId.setValue(result.get("result"));
                } else {
                    initResult.setValue(false);
                    errorMessage.setValue("Try again");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call,
                                  @NonNull Throwable t) {
                initResult.setValue(false);
                errorMessage.setValue("Trouble with init. Try again.");
            }
        });
    }

    public void createAccount(ApiInitService api, String email, String userName) {
        Call<Map<String,Boolean>> call = api.createNewAccount(email, userName);
        call.enqueue(new  Callback<>(){

            @Override
            public void onResponse(@NonNull Call<Map<String, Boolean>> call,
                                   @NonNull Response<Map<String, Boolean>> response) {
                if (response.isSuccessful()){
                    Map<String,Boolean> result = response.body();
                    if (result != null) {
                        createAccResult.setValue(result.get("result"));
                    }
                } else {
                    createAccResult.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Boolean>> call,
                                  @NonNull Throwable t) {
                createAccResult.setValue(false);
            }
        });
    }

    public void getStatusUser(ApiInitService api) {
        Call<Map<String, String>> call = api.userStatus();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call,
                                   @NonNull Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    if (response.body().get("status") != null) {
                        sessionId.setValue(response.body().get("status"));
                    } else {
                        sessionId.setValue("null");
                    }
                } else {
                    sessionId.setValue("null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call,
                                  @NonNull Throwable t) {
                sessionId.setValue("null");
            }
        });
    }

    public LiveData<Boolean> getInitResult() {
        return initResult;
    }

    public LiveData<Boolean> getAuthResult() {
        return authResult;
    }

    public LiveData<Boolean> getCreateAccResult() {
        return createAccResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSessionId() {
        return sessionId;
    }
}