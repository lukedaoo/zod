package com.infamous.framework.sensitive.service;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import java.security.SecureRandom;

public class SaltedPasswordEncryptor extends DefaultPasswordEncryptor implements PasswordEncryptor {

    private static final int MAX_SEED = 99999999;
    private static final int HASH_ITERATIONS = 1000;

    private final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String encrypt(MessageDigestAlgorithm algorithm, String sensitiveString) {
        String seed = Integer.toString(RANDOM.nextInt(MAX_SEED));

        return encrypt(algorithm, sensitiveString, seed);
    }

    public String encrypt(MessageDigestAlgorithm algorithm, String sensitiveString, String salt) {
        String hash = salt + sensitiveString;
        for (int i = 0; i < HASH_ITERATIONS; i++) {
            hash = algorithm.hash(hash);
        }
        return salt + ":" + hash;
    }

    @Override
    public boolean matches(MessageDigestAlgorithm algorithm, String passwordToCheck, String storedPassword) {
        if (passwordToCheck == null) {
            throw new IllegalArgumentException("passwordToCheck can not be null");
        }
        if (storedPassword == null) {
            throw new IllegalArgumentException("storedPassword can not be null");
        }

        int divider = storedPassword.indexOf(':');

        if (divider < 1) {
            throw new IllegalArgumentException("storedPassword does not contain salt");
        }

        String storedSalt = storedPassword.substring(0, divider);

        return encrypt(algorithm, passwordToCheck, storedSalt).equalsIgnoreCase(storedPassword);
    }
}
