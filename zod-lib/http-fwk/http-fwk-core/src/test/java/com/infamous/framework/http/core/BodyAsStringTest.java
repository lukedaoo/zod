package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class BodyAsStringTest {

    @Test
    public void test() {
        BodyAsString body = new BodyAsString("jsonString");

        assertNull(body.getFileName());
        assertFalse(body.isFile());
        assertEquals("jsonString", body.getValue());
    }
}