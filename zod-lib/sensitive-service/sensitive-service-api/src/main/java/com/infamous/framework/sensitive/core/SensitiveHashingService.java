package com.infamous.framework.sensitive.core;

import java.security.NoSuchAlgorithmException;

public interface SensitiveHashingService {

    String hash(String obj, MessageDigestAlgorithm algorithm) throws NoSuchAlgorithmException;

    default String hash(String obj) throws NoSuchAlgorithmException {
        return hash(obj, MessageDigestAlgorithm.SHA_256);
    }
}