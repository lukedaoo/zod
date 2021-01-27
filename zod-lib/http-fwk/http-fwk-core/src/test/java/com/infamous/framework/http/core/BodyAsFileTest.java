package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

class BodyAsFileTest {

    @Test
    public void test() {
        BodyAsFile body = new BodyAsFile(new File("test.txt"));

        assertNull(body.getFileName());
        assertFalse(body.isFile());
        assertTrue(body.getValue() instanceof File);
    }
}