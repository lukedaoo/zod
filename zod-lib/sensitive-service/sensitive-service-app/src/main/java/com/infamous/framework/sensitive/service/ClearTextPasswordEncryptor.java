package com.infamous.framework.sensitive.service;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;

public class ClearTextPasswordEncryptor implements PasswordEncryptor {

    @Override
    public String encrypt(MessageDigestAlgorithm algorithm, String sensitiveString) {
        return sensitiveString;
    }

    @Override
    public boolean matches(MessageDigestAlgorithm algorithm, String passwordToCheck, String storedPassword) {
        return passwordToCheck.equals(storedPassword);
    }
}
