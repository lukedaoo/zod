package com.infamous.framework.sensitive.core;

public interface SensitiveHashingService {

    String hash(String obj, MessageDigestAlgorithm algorithm);

    default String hash(String obj) {
        return hash(obj, MessageDigestAlgorithm.SHA_256);
    }
}