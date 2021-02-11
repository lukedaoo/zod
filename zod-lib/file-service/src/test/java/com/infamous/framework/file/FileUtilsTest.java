package com.infamous.framework.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import org.junit.jupiter.api.Test;

class FileUtilsTest {


    @Test
    public void testGetExt() {
        assertNull(FileUtils.getExtension(""));
        assertNull(FileUtils.getExtension(null));
        assertEquals("xyz", FileUtils.getExtension("abc.xyz"));
        assertEquals("gz", FileUtils.getExtension("abc.tar.gz"));
        assertEquals("x", FileUtils.getExtension("xyz.xyA.x"));
    }

    @Test
    public void testMd5() {
        assertNotNull(FileUtils.md5(new File("src/test/resources/existed/Kamehameha.txt")));
        assertNotNull(FileUtils.md5("src/test/resources/existed/Kamehameha.txt"));
        assertNull(FileUtils.md5("src/test/resources/no_existed/Kamehameha.txt"));
    }
}