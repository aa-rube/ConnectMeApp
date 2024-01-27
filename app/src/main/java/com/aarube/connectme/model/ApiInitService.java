package com.aarube.connectme.model;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInitService {
    @GET("/init")
    Call<Map<String, String>> init(@Query("authSecure") String authCrypto);

    @POST("/auth")
    Call<Map<String, Boolean>> auth(@Query("email") String email);

    @POST("/new_account")
    Call<Map<String, Boolean>> createNewAccount(@Query("encryptedEmail") String encryptedEmail,
                                                @Query("username") String username);

    @POST("/userStatus")
    Call<Map<String, String>> userStatus();
}

