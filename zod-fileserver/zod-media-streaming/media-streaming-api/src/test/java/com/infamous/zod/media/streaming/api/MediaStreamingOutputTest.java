package com.infamous.zod.media.streaming.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class MediaStreamingOutputTest {


    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("src/test/resources/dragonball/skills_copy.txt"));
    }

    @Test
    public void testWrite() throws Exception {
        File file = new File("src/test/resources/dragonball/skills.txt");
        assertTrue(file.exists());
        MediaStreamingOutput output = new MediaStreamingOutput((int) file.length(), 0, file);

        OutputStream fileOs = new FileOutputStream(new File("src/test/resources/dragonball/skills_copy.txt"));

        output.write(fileOs);

        assertTrue(Files.exists(Path.of("src/test/resources/dragonball/skills_copy.txt")));
    }
}