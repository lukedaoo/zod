package com.infamous.framework.sensitive.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import org.junit.jupiter.api.Test;

class SaltedPasswordEncryptorTest {


    @Test
    public void testEncrypt() {
        SaltedPasswordEncryptor saltedPE = new SaltedPasswordEncryptor();
        String hashed = saltedPE.encrypt(MessageDigestAlgorithm.MD5, "123", "salted_test");

        assertTrue(hashed.contains("salted_test"));
        assertNotEquals("123", hashed);

        assertTrue(saltedPE.matches("123", "salted_test:72259c02fd9ac261fad7eb5d16fef90d"));
    }

    @Test
    public void testEncrypt_randomSalted() {
        SaltedPasswordEncryptor saltedPE = new SaltedPasswordEncryptor();
        String hashed = saltedPE.encrypt(MessageDigestAlgorithm.MD5, "123");

        System.out.println(hashed);
        assertNotEquals("123", hashed);
    }

    @Test
    public void testEncrypt_defaultMD5_Validation() {
        checkErrorMessage(null, "123", "passwordToCheck can not be null");
        checkErrorMessage("123", null, "storedPassword can not be null");
        checkErrorMessage("123", null, "storedPassword can not be null");
        checkErrorMessage("123", "123", "storedPassword does not contain salt");
    }

    private void checkErrorMessage(final String passwordToCheck, final String storedPassword,
                                   String expectedErrorMessage) {
        SaltedPasswordEncryptor saltedPE = new SaltedPasswordEncryptor();

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> saltedPE.matches(passwordToCheck, storedPassword));

        assertEquals(exception.getMessage(), expectedErrorMessage);
    }

}