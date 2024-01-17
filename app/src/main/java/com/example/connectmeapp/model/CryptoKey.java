package com.example.connectmeapp.model;

public class CryptoKey {
    public static String fullKey(String email, String key) {
        return email + ":" + key;
    }
}
