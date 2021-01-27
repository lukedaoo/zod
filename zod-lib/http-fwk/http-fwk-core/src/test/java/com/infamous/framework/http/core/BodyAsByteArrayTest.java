package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class BodyAsByteArrayTest {

    @Test
    public void test() {
        byte[] bytes = new byte[1];
        bytes[0] = 1;

        BodyAsByteArray body = new BodyAsByteArray(bytes);

        assertNull(body.getFileName());
        assertFalse(body.isFile());
        assertEquals(1, body.getValue()[0]);
    }
}