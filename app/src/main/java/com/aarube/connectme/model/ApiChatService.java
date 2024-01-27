package com.aarube.connectme.model;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiChatService {
    @GET("/user")
    Call<Map<String, Integer>> getUsersList(@Query("sessionId") String sessionId);
}
