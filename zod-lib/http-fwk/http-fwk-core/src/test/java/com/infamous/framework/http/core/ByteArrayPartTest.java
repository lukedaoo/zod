package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ByteArrayPartTest {

    @Test
    public void test() {
        ByteArrayPart byteArrayPart = new ByteArrayPart("file", new byte[0], "application/file", "test.txt");

        assertEquals("test.txt", byteArrayPart.getFileName());
        assertTrue(byteArrayPart.isFile());
    }
}