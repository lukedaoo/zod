package com.infamous.framework.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.infamous.framework.logging.core.LogKey;
import org.junit.jupiter.api.Test;

class ZodLoggerUtilTest {


    @Test
    public void testZodLoggerUtil() {
        ZodLogger logger = ZodLoggerUtil.getLogger(ZodLoggerUtilTest.class, "log.testing");

        assertEquals("log.testing", logger.getName());
        assertEquals(new LogKey("log.testing", "debug", "global"), logger.getKey());
    }

    @Test
    public void testZodLoggerUtil_ByCategory_UnsupportedPrefix() {
        assertThrows(IllegalArgumentException.class, () -> ZodLoggerUtil.getLogger("log.category", "log.testing"));
    }

    @Test
    public void testZodLoggerUtil_ByCategory() {
        ZodLogger logger = ZodLoggerUtil.getLogger("com.infamous.log.category", "log.testing");
        assertEquals("log.testing", logger.getName());
        assertEquals(new LogKey("log.testing", "debug", "global"), logger.getKey());
    }
}