package com.infamous.framework.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infamous.framework.logging.core.LogKey;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZodLoggerFactoryTest {


    @BeforeEach
    public void setup() {
        ZodLoggerFactory.getInstance().reset();
    }

    @Test
    public void testUsingHashingService_enableIncludingRawValue() {
        ZodLoggerFactory.getInstance().useSensitiveHashingService(ZodSensitiveHashingService::enabledIncludingRawValue);
        ZodLogger zodLogger = ZodLoggerFactory.getInstance()
            .getLogger(ZodLoggerFactoryTest.class, "testing.logging", "debug", "global");

        String mess = "ABC " + zodLogger.sensitiveObject("123");
        zodLogger.debug(mess);
        assertTrue(mess.startsWith("ABC ####123####"));
    }

    @Test
    public void testUsingHashingService_disableIncludingRawValue() {
        ZodLoggerFactory.getInstance()
            .useSensitiveHashingService(ZodSensitiveHashingService::disabledIncludingRawValue);
        ZodLogger zodLogger = ZodLoggerFactory.getInstance()
            .getLogger(ZodLoggerFactoryTest.class, "testing.logging", "debug", "global");

        String mess = "ABC " + zodLogger.sensitiveObject("123");
        zodLogger.debug(mess);
        assertFalse(mess.startsWith("ABC ####123####"));
    }

    @Test
    public void testGetLogger_WithSameCategory() {
        ZodLoggerFactory.getInstance()
            .getLogger("com.infamous.testing", "testing.logging1", "debug", "global");

        ZodLoggerFactory.getInstance()
            .getLogger("com.infamous.testing", "testing.logging2", "debug", "global");

        assertEquals(2, ZodLoggerFactory.getInstance().getLogKeyRegister().size());
        assertEquals(Set.of("com.infamous.testing"),
            ZodLoggerFactory.getInstance().getCategoriesByLogKey(new LogKey("testing.logging1", "debug", "global")));
        assertEquals(Set.of("com.infamous.testing#0"),
            ZodLoggerFactory.getInstance().getCategoriesByLogKey(new LogKey("testing.logging2", "debug", "global")));
    }
}