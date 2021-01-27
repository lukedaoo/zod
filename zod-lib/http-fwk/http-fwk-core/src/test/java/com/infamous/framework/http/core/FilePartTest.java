package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

class FilePartTest {


    @Test
    public void test() {
        FilePart filePart = new FilePart("file", new File("test.txt"), "application/file");

        assertEquals("test.txt", filePart.getFileName());
        assertTrue(filePart.isFile());
    }
}