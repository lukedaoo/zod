package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class UtilsTest {


    @Test
    public void testMethodThrow() {
        Method method = MockTesting.class.getMethods()[0];
        Exception exp = Utils.methodError(method, "I can explain");
        assertEquals("I can explain for method MockTesting.apply", exp.getMessage());
    }

    @Test
    public void testMethodThrowWithCause() {
        Method method = MockTesting.class.getMethods()[0];
        Exception exp = Utils
            .methodError(method, new RuntimeException("I dont understand"), "I can explain %s", "Attack on Titan");
        assertEquals("I can explain Attack on Titan for method MockTesting.apply", exp.getMessage());
        assertEquals("I dont understand", exp.getCause().getMessage());
    }

    @Test
    public void testParamThrow() {
        Method method = MockTesting.class.getMethods()[0];
        Exception exp = Utils.parameterError(method, 0, "ok bruh");
        assertEquals("ok bruh (parameter #1) for method MockTesting.apply", exp.getMessage());
    }

    @Test
    public void testParamThrowWithCause() {
        Method method = MockTesting.class.getMethods()[0];
        Exception exp = Utils.parameterError(method, new RuntimeException("I dont understand"), 0, "ok bruh");
        assertEquals("ok bruh (parameter #1) for method MockTesting.apply", exp.getMessage());
        assertEquals("I dont understand", exp.getCause().getMessage());
    }


}

class MockTesting {


    public void apply(String a, int b) {

    }
}