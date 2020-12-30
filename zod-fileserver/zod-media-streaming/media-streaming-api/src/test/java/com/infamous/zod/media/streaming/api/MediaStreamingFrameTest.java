package com.infamous.zod.media.streaming.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class MediaStreamingFrameTest {

    @Test
    public void testCreate() throws IOException {
        MediaStreamingOutput output = new MediaStreamingOutput(100, 1,
            new File("src/test/resources/dragonball/skills.txt"));
        MediaRange range = MediaRange.of(1, 10, 100);
        MediaStreamingFrame frame = MediaStreamingFrame.of(output, range);

        assertNotNull(frame.getOutput());
        assertNotNull(frame.getRange());
    }
}