package com.infamous.zod.ftp.um.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EncryptStrategyTest {


    @Test
    void testFind() {
        test(EncryptStrategy.MD5, new String[]{"md5", "mD5", "MD5"});
        test(EncryptStrategy.CLEAR, new String[]{"clear", "CLEAR", "clEAR"});
        test(EncryptStrategy.SALTED, new String[]{"salted", "SALTED", "SAlted"});

        assertThrows(IllegalArgumentException.class, () -> EncryptStrategy.find(null));
        assertThrows(IllegalArgumentException.class, () -> EncryptStrategy.find(""));
        assertThrows(IllegalArgumentException.class, () -> EncryptStrategy.find("ABC"));
    }

    void test(EncryptStrategy encryptStrategy, String[] input) {
        assertEquals(encryptStrategy, EncryptStrategy.find(input[0]));
        assertEquals(encryptStrategy, EncryptStrategy.find(input[1]));
        assertEquals(encryptStrategy, EncryptStrategy.find(input[2]));
    }
}