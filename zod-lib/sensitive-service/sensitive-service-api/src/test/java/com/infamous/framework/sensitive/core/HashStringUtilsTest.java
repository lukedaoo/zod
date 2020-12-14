package com.infamous.framework.sensitive.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class HashStringUtilsTest {


    @Test
    public void testHash() throws NoSuchAlgorithmException {
        String exp = HashStringUtils.hash(MessageDigest.getInstance("MD5"), "123");

        assertNotNull(exp);
    }

    @Test
    public void testHashThrowEx() throws NoSuchAlgorithmException {
        assertThrows(NullPointerException.class, () -> HashStringUtils.hash(null, "123"));
        assertThrows(NullPointerException.class, () -> HashStringUtils.hash(MessageDigest.getInstance("MD5"), null));
    }
}