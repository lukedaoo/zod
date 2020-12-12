package com.infamous.framework.sensitive.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import org.junit.jupiter.api.Test;

class ClearTextPasswordEncryptorTest {

    @Test
    public void testEncrypt() {
        ClearTextPasswordEncryptor clearTextPasswordEncryptor = new ClearTextPasswordEncryptor();

        assertEquals("123", clearTextPasswordEncryptor.encrypt("123"));
        assertEquals("123", clearTextPasswordEncryptor.encrypt(MessageDigestAlgorithm.SHA_256, "123"));
        assertTrue(clearTextPasswordEncryptor.matches("123", "123"));
        assertTrue(clearTextPasswordEncryptor.matches(MessageDigestAlgorithm.SHA_512, "123", "123"));
    }
}