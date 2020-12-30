package com.infamous.zod.media.streaming.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeekableByteArrayInputStreamTest {

    private byte[] m_file;

    @BeforeEach
    public void setup() {
        File file = new File("src/test/resources/dragonball/skills.txt");

        m_file = new byte[(int) file.length()];
        try (FileInputStream is = new FileInputStream(file)) {
            is.read(m_file);
        } catch (Exception e) {
            //ignore
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        m_file = null;
        Files.deleteIfExists(Path.of("src/test/resources/dragonball/skills_skip.txt"));
    }

    @Test
    public void testSeek() {
        SeekableByteArrayInputStream is = new SeekableByteArrayInputStream(m_file, m_file.length);

        is.seek(10);

        File file = new File("src/test/resources/dragonball/skills.txt");

        assertEquals(file.length() - 10, is.available());
    }

    @Test
    public void testRead() throws IOException {
        SeekableByteArrayInputStream is = new SeekableByteArrayInputStream(m_file, m_file.length);
        is.seek(100); // skip first 100 bytes
        byte[] buffer = new byte[m_file.length];

        try (FileOutputStream os = new FileOutputStream("src/test/resources/dragonball/skills_skip.txt")) {
            int read = is.read(buffer, 0, 100);
            while (read >= 0) {
                os.write(buffer, 0, read);
                read = is.read(buffer, 0, 100);
            }
        }

        File output = new File("src/test/resources/dragonball/skills_skip.txt");
        assertTrue(output.exists());
        assertEquals(m_file.length - 100, output.length());
    }
}