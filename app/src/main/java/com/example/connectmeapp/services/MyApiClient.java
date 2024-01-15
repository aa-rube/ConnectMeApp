package com.example.connectmeapp.services;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApiClient {
    private static final String BASE_URL = "https://192.168.0.11:8443/";

    public static Retrofit getApiClient() {
        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a custom TrustManager that trusts the server's self-signed certificate
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new CustomTrustManager()},
                    new java.security.SecureRandom());

            // Apply the custom TrustManager to OkHttpClient
            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), new CustomTrustManager())
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}