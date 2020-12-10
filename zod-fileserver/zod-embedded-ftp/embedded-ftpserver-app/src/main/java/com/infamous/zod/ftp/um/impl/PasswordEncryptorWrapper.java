package com.infamous.zod.ftp.um.impl;

import java.security.SecureRandom;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.util.EncryptUtils;

public class PasswordEncryptorWrapper implements PasswordEncryptor {

    private PasswordEncryptor m_internal;

    public PasswordEncryptorWrapper(EncryptStrategy encryptStrategy) {
        if (encryptStrategy == null) {
            // by default
            m_internal = new Md5PasswordEncryptor();
        } else if (encryptStrategy == EncryptStrategy.MD5) {
            m_internal = new Md5PasswordEncryptor();
        } else if (encryptStrategy == EncryptStrategy.SALTED) {
            m_internal = new SaltedPasswordEncryptor();
        } else {
            m_internal = new ClearTextPasswordEncryptor();
        }
    }

    @Override
    public String encrypt(String password) {
        return m_internal.encrypt(password);
    }

    @Override
    public boolean matches(String passwordToCheck, String storedPassword) {
        return m_internal.matches(passwordToCheck, storedPassword);
    }

    public PasswordEncryptor getInternalService() {
        return m_internal;
    }

    public static class SaltedPasswordEncryptor implements PasswordEncryptor {

        private SecureRandom rnd = new SecureRandom();

        private static final int MAX_SEED = 99999999;
        private static final int HASH_ITERATIONS = 1000;

        public String encrypt(String password, String salt) {
            String hash = salt + password;
            for (int i = 0; i < HASH_ITERATIONS; i++) {
                hash = EncryptUtils.encryptMD5(hash);
            }
            return salt + ":" + hash;
        }

        public String encrypt(String password) {
            String seed = Integer.toString(rnd.nextInt(MAX_SEED));

            return encrypt(password, seed);
        }

        public boolean matches(String passwordToCheck, String storedPassword) {
            if (storedPassword == null) {
                throw new NullPointerException("storedPassword can not be null");
            }
            if (passwordToCheck == null) {
                throw new NullPointerException("passwordToCheck can not be null");
            }

            // look for divider for hash
            int divider = storedPassword.indexOf(':');

            if(divider < 1) {
                throw new IllegalArgumentException("stored password does not contain salt");
            }

            String storedSalt = storedPassword.substring(0, divider);

            return encrypt(passwordToCheck, storedSalt).equalsIgnoreCase(storedPassword);
        }
    }

}

