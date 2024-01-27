package com.aarube.connectme.model;

public class CryptoKey {
    public static String fullKey(String email, String key) {
        return email + ":" + key;
    }
}
