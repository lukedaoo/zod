package com.infamous.zod.ftp.um.impl;

import java.util.Arrays;

public enum EncryptStrategy {

    MD5,
    SALTED,
    CLEAR;

    EncryptStrategy() {
    }

    public static EncryptStrategy find(String str) {
        return Arrays.stream(EncryptStrategy.values())
            .filter(e -> e.toString().toLowerCase().equalsIgnoreCase(str))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported strategy " + str));
    }
}