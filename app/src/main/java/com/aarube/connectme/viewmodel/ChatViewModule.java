package com.aarube.connectme.viewmodel;

import androidx.annotation.NonNull;

import com.aarube.connectme.model.ApiChatService;
import com.aarube.connectme.model.ApiInitService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModule {
//    public void getUsersList(ApiChatService api, String sessionId) {
//        Call<Map<String, Integer>> call = api.getUsersList(sessionId);
//        return call.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(@NonNull Call<Map<String, Integer>> call,
//                                   @NonNull Response<Map<String, Integer>> response) {
//
//                if (response.isSuccessful()) {
//                    Map<String, Integer> result = response.body();
//                    if (result != null && Boolean.TRUE.equals(result.get(email))) {
//                        authResult.setValue(true);
//                    } else {
//                        authResult.setValue(false);
//                        errorMessage.setValue("Try again");
//                    }
//
//                } else {
//                    authResult.setValue(false);
//                    errorMessage.setValue("Try again");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
//
//            }
//
//        });
//    }
}
