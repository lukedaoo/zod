package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import org.junit.jupiter.api.Test;

class BodyAsInputStreamTest {

    @Test
    public void test() {
        BodyAsInputStream body = new BodyAsInputStream(mock(InputStream.class));

        assertNull(body.getFileName());
        assertFalse(body.isFile());
        assertTrue(body.getValue() instanceof InputStream);
    }
}