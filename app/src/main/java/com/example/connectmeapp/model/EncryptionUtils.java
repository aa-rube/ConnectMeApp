package com.example.connectmeapp.model;
public class EncryptionUtils {
    public static String encrypt(String value, String password) {
        return value + ", " +  password;
    }
}

