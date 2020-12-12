package com.infamous.framework.sensitive.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import org.junit.jupiter.api.Test;

class DefaultPasswordEncryptorTest {

    @Test
    public void testEncrypt_defaultMD5() {
        DefaultPasswordEncryptor defaultPasswordEncryptor = new DefaultPasswordEncryptor();

        String hashed = defaultPasswordEncryptor.encrypt("123");

        assertNotNull(hashed);
        // MD5
        assertEquals("202cb962ac59075b964b07152d234b70", hashed);
        assertTrue(defaultPasswordEncryptor.matches("123", "202cb962ac59075b964b07152d234b70"));
    }

    @Test
    public void testEncrypt() {
        DefaultPasswordEncryptor defaultPasswordEncryptor = new DefaultPasswordEncryptor();

        String hashed = defaultPasswordEncryptor.encrypt(MessageDigestAlgorithm.SHA_512, "123");

        assertNotNull(hashed);
        // MD5
        assertEquals(
            "3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2",
            hashed);
        assertTrue(defaultPasswordEncryptor
            .matches(MessageDigestAlgorithm.SHA_512, "123",
                "3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2"));
    }


    @Test
    public void testEncrypt_defaultMD5_Validation() {
        checkErrorMessage(null, "123", "passwordToCheck can not be null");
        checkErrorMessage("123", null, "storedPassword can not be null");
    }

    private void checkErrorMessage(final String passwordToCheck, final String storedPassword,
                                   String expectedErrorMessage) {
        DefaultPasswordEncryptor defaultPasswordEncryptor = new DefaultPasswordEncryptor();

        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> defaultPasswordEncryptor.matches(passwordToCheck, storedPassword));

        assertEquals(exception.getMessage(), expectedErrorMessage);
    }
}