package com.infamous.framework.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemPropertyUtilsTest {


    @BeforeEach
    public void setupEnv() {
        System.setProperty("db.user", "admin");

        System.setProperty("isSSL", "false");
        System.setProperty("shouldUse", "12344");

        System.setProperty("intValue", "123");
        System.setProperty("invalidIntValue", "abc");

        System.setProperty("longValue", "1234");
        System.setProperty("invalidLongValue", "abcd");

    }

    @AfterEach
    public void reset() {
        System.clearProperty("db.user");

        System.clearProperty("isSSL");
        System.clearProperty("shouldUse");

        System.clearProperty("intValue");
        System.clearProperty("invalidIntValue");

        System.clearProperty("longValue");
        System.clearProperty("invalidLongValue");
    }


    @Test
    public void testGetProp() {
        assertEquals(SystemPropertyUtils.getInstance().getProperty("db.user", null), "admin");
        assertEquals(SystemPropertyUtils.getInstance().getProperty("db.password", "admin"), "admin");
    }

    @Test
    public void testGetPropAsBoolean() {
        assertFalse(SystemPropertyUtils.getInstance().getAsBoolean("isSSL", true));
        assertTrue(SystemPropertyUtils.getInstance().getAsBoolean("isSSLNotFound", true));
        assertTrue(SystemPropertyUtils.getInstance().getAsBoolean("shouldUse", true));
    }

    @Test
    public void testGetPropAsInt() {
        assertEquals(SystemPropertyUtils.getInstance().getAsInt("intValue", 1), 123);
        assertEquals(SystemPropertyUtils.getInstance().getAsInt("intValueNotFound", 12), 12);
        assertEquals(SystemPropertyUtils.getInstance().getAsInt("invalidIntValue", 99), 99);
    }
    @Test
    public void testGetPropAsLong() {
        assertEquals(SystemPropertyUtils.getInstance().getAsLong("longValue", 1), 1234);
        assertEquals(SystemPropertyUtils.getInstance().getAsLong("longValueNotFound", 12), 12);
        assertEquals(SystemPropertyUtils.getInstance().getAsLong("invalidLongValue", 12345), 12345);
    }
}