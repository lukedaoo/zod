package com.infamous.zod.media.streaming.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MediaRangeTest {

    @Test
    public void testInit() {
        MediaRange range = MediaRange.of(1, 10, 100);
        assertEquals(1, range.getFrom());
        assertEquals(10, range.getTo());
        assertEquals(100, range.getLimit());
        assertEquals(10, range.calculateLength());
        assertEquals("bytes 1-10/100", range.generateRangeString());
    }

    @Test
    public void testInit_WithToIsEqualLimit() {
        MediaRange range = MediaRange.of(1, 100, 100);
        assertEquals(1, range.getFrom());
        assertEquals(99, range.getTo());
        assertEquals(100, range.getLimit());
        assertEquals(99, range.calculateLength());
        assertEquals("bytes 1-99/100", range.generateRangeString());
    }

    @Test
    public void testInit_WithRangeString() {
        MediaRange range = MediaRange.of("bytes=0-1000", 9000);
        assertEquals(0, range.getFrom());
        assertEquals(1000, range.getTo());
        assertEquals(9000, range.getLimit());
        assertEquals(1001, range.calculateLength());
        assertEquals("bytes 0-1000/9000", range.generateRangeString());
    }

    @Test
    public void testInit_WithRangeString_WithoutTo() {
        MediaRange range = MediaRange.of("bytes=1-", 9000 + MediaStreamingFrame.CHUNK_SIZE);
        assertEquals(1, range.getFrom());
        assertEquals(1 + MediaStreamingFrame.CHUNK_SIZE, range.getTo());
        assertEquals(9000 + MediaStreamingFrame.CHUNK_SIZE, range.getLimit());
        assertEquals(1 + MediaStreamingFrame.CHUNK_SIZE, range.calculateLength());
        assertEquals("bytes 1-" + (1 + MediaStreamingFrame.CHUNK_SIZE) + "/" + (9000 + MediaStreamingFrame.CHUNK_SIZE),
            range.generateRangeString());
    }

    @Test
    public void testInit_WithRangeString_WrongFormat() {
        assertThrows(IllegalArgumentException.class,
            () -> MediaRange.of("bytes-", 9000 + MediaStreamingFrame.CHUNK_SIZE));
    }
}