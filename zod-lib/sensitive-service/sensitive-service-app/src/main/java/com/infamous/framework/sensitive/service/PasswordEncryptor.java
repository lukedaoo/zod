package com.infamous.framework.sensitive.service;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;

public interface PasswordEncryptor {

    String encrypt(MessageDigestAlgorithm algorithm, String sensitiveString);

    default String encrypt(String sensitiveString) {
        return encrypt(MessageDigestAlgorithm.MD5, sensitiveString);
    }

    boolean matches(MessageDigestAlgorithm algorithm, String passwordToCheck, String storedPassword);

    default boolean matches(String passwordToCheck, String storedPassword) {
        return matches(MessageDigestAlgorithm.MD5, passwordToCheck, storedPassword);
    }
}
