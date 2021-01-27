package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputStreamPartTest {

    private InputStream m_inputStreamMock;

    @BeforeEach
    public void setup() {
        m_inputStreamMock = mock(InputStream.class);
    }

    @Test
    public void testNewInputStreamPart() {
        InputStreamPart part = new InputStreamPart("file", m_inputStreamMock, "application/octet-stream");

        assertTrue(part.isFile());
        assertNotNull(part.getFileName());
        assertEquals("file", part.getName());
        assertEquals("application/octet-stream", part.getContentType());
    }

    @Test
    public void testNewInputStreamPart_WithName() {
        InputStreamPart part = new InputStreamPart("file", m_inputStreamMock, "application/octet-stream", "demo.txt");

        assertTrue(part.isFile());
        assertEquals("demo.txt", part.getFileName());
        assertEquals("file", part.getName());
        assertEquals("application/octet-stream", part.getContentType());
    }
}