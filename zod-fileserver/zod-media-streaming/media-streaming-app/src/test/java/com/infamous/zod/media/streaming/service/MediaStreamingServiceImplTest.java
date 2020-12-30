package com.infamous.zod.media.streaming.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.infamous.zod.media.streaming.api.MediaStreamingFrame;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaStreamingServiceImplTest {

    private MediaStreamingServiceImpl m_mediaStreamingService;

    @BeforeEach
    public void setup() {
        m_mediaStreamingService = new MediaStreamingServiceImpl();
    }

    @Test
    public void testMakeFrame() {
        File file = new File("src/test/resources/attackontitan/chars.txt");
        String range = "bytes=0-100";
        MediaStreamingFrame frame = m_mediaStreamingService.makeFrame(file, range);

        assertNotNull(frame);
        assertEquals(0, frame.getRange().getFrom());
        assertEquals(100, frame.getRange().getTo());

        assertEquals(101, frame.getOutput().getLength());
    }

    @Test
    public void testMakeFrame_InvalidRange() {
        File file = new File("src/test/resources/attackontitan/chars.txt");
        String range = "bytes-100";
        IllegalArgumentException exp = assertThrows(IllegalArgumentException.class,
            () -> m_mediaStreamingService.makeFrame(file, range));
        assertEquals("The format of rangeString is invalid. Range: bytes-100", exp.getMessage());
    }

    @Test
    public void testMakeFrame_WhenFileDoesNotExist() {
        File file = new File("src/test/resources/attackontitan/chars-test.txt");
        String range = "bytes=0-100";
        IllegalStateException exp = assertThrows(IllegalStateException.class,
            () -> m_mediaStreamingService.makeFrame(file, range));
        assertEquals("Empty frame is created", exp.getMessage());
    }
}