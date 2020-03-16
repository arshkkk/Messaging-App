package com.example.whatsapp;

public class PrivateKey {
    private static String privateKey;

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(String privateKey) {
        PrivateKey.privateKey = privateKey;
    }
}
