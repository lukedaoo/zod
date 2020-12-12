package com.infamous.framework.sensitive.service;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;

public class DefaultPasswordEncryptor implements PasswordEncryptor {

    public DefaultPasswordEncryptor() {

    }

    @Override
    public String encrypt(MessageDigestAlgorithm algorithm, String sensitiveString) {
        return algorithm.hash(sensitiveString);
    }

    @Override
    public boolean matches(MessageDigestAlgorithm algorithm, String passwordToCheck, String storedPassword) {
        if (passwordToCheck == null) {
            throw new IllegalArgumentException("passwordToCheck can not be null");
        }
        if (storedPassword == null) {
            throw new IllegalArgumentException("storedPassword can not be null");
        }

        return encrypt(algorithm, passwordToCheck).equalsIgnoreCase(storedPassword);
    }
}
