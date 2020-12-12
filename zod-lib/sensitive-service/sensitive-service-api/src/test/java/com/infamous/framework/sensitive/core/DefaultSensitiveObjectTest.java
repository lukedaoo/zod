package com.infamous.framework.sensitive.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DefaultSensitiveObjectTest {


    @Test
    public void testToString() {
        DefaultSensitiveObject obj = new DefaultSensitiveObject("123");
        assertNotNull(obj.toString());
        assertNotEquals("123", obj.toString());
    }


    @Test
    public void testToString_Null() {
        DefaultSensitiveObject obj = new DefaultSensitiveObject(null);
        assertNotNull(obj.toString());
        assertNotEquals("123", obj.toString());
    }
}