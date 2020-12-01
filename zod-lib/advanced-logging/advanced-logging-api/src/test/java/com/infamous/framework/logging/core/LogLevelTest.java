package com.infamous.framework.logging.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

@DisplayName("LogLevel Test Suite")
class LogLevelTest {

    @Test
    public void testFromLevelWithNullStr() {
        String input = null;
        assertNull(input);
        assertThrows(IllegalArgumentException.class, () -> LogLevel.fromLevel(Optional.ofNullable(input)));
    }

    @Test
    public void testFromLevelWithInvalidStr() {
        String input = "SOMETHING_ELSE";
        assertThrows(IllegalArgumentException.class, () -> LogLevel.fromLevel(Optional.ofNullable(input)));
    }

    @Test
    public void testFromLevelWithValidStr_Uppercase() {
        assertFromLevel("DEBUG", LogLevel.DEBUG);
        assertFromLevel("INFO", LogLevel.INFO);
        assertFromLevel("WARNING", LogLevel.WARNING);
        assertFromLevel("ERROR", LogLevel.ERROR);
        assertFromLevel("TRACE", LogLevel.TRACE);
    }

    @Test
    public void testFromLevelWithValidStr_Lowercase() {
        assertFromLevel("debug", LogLevel.DEBUG);
        assertFromLevel("info", LogLevel.INFO);
        assertFromLevel("warning", LogLevel.WARNING);
        assertFromLevel("error", LogLevel.ERROR);
        assertFromLevel("trace", LogLevel.TRACE);
    }

    @Test
    public void testFromLevelWithValidStr_Mixed() {
        assertFromLevel("deBug", LogLevel.DEBUG);
        assertFromLevel("infO", LogLevel.INFO);
        assertFromLevel("warniNg", LogLevel.WARNING);
        assertFromLevel("erroR", LogLevel.ERROR);
        assertFromLevel("Trace", LogLevel.TRACE);
    }


    private void assertFromLevel(String input, LogLevel expected) {
        assertEquals(expected, LogLevel.fromLevel(Optional.of(input)));
    }

    @Test
    void isDisabled() {
        assertTrue(LogLevel.fromLevel(Optional.of("disabled")).isDisabled());
        assertFalse(LogLevel.fromLevel(Optional.of("debug")).isDisabled());
        assertFalse(LogLevel.fromLevel(Optional.of("info")).isDisabled());
        assertFalse(LogLevel.fromLevel(Optional.of("error")).isDisabled());
        assertFalse(LogLevel.fromLevel(Optional.of("warning")).isDisabled());
        assertFalse(LogLevel.fromLevel(Optional.of("trace")).isDisabled());
    }

    private TestDefaultLogger m_logger;
    private org.slf4j.Logger m_target;

    @BeforeEach
    public void setup() {
        m_logger = DefaultLoggerFactory.getLogger("LogLevelTest");
        m_target = mock(Logger.class);
        m_logger.setSlf4jLogger(m_target);
    }

    @Test
    public void testInvoke() {
        LogLevel.DEBUG.getConsumerInvokeLog().accept(m_logger, new LogMessage("Debug messages", null));
        verify(m_target).debug(eq("Debug messages"));

        LogLevel.INFO.getConsumerInvokeLog().accept(m_logger, new LogMessage("Info messages", null));
        verify(m_target).info(eq("Info messages"));

        LogLevel.WARNING.getConsumerInvokeLog().accept(m_logger, new LogMessage("Warning messages", null));
        verify(m_target).warn(eq("Warning messages"));

        LogLevel.ERROR.getConsumerInvokeLog().accept(m_logger, new LogMessage("Error messages", null));
        verify(m_target).error(eq("Error messages"));

        LogLevel.TRACE.getConsumerInvokeLog().accept(m_logger, new LogMessage("Trace messages", null));
        verify(m_target).trace(eq("Trace messages"));
    }

    @Test
    public void testCheckEnabled() {
        when(m_target.isDebugEnabled()).thenReturn(true);
        when(m_target.isInfoEnabled()).thenReturn(false);
        when(m_target.isWarnEnabled()).thenReturn(false);
        when(m_target.isErrorEnabled()).thenReturn(false);
        when(m_target.isTraceEnabled()).thenReturn(false);

        assertTrue(LogLevel.DEBUG.getIsEnabledFunction().apply(m_logger));
        verify(m_target).isDebugEnabled();

        assertFalse(LogLevel.WARNING.getIsEnabledFunction().apply(m_logger));
        verify(m_target).isWarnEnabled();

        assertFalse(LogLevel.INFO.getIsEnabledFunction().apply(m_logger));
        verify(m_target).isInfoEnabled();

        assertFalse(LogLevel.ERROR.getIsEnabledFunction().apply(m_logger));
        verify(m_target).isErrorEnabled();

        assertFalse(LogLevel.TRACE.getIsEnabledFunction().apply(m_logger));
        verify(m_target).isTraceEnabled();

        when(m_target.isDebugEnabled()).thenReturn(false);
        when(m_target.isInfoEnabled()).thenReturn(false);
        when(m_target.isWarnEnabled()).thenReturn(false);
        when(m_target.isErrorEnabled()).thenReturn(false);
        when(m_target.isTraceEnabled()).thenReturn(false);
        assertTrue(LogLevel.DISABLED.getIsEnabledFunction().apply(m_logger));

        when(m_target.isDebugEnabled()).thenReturn(false);
        when(m_target.isInfoEnabled()).thenReturn(false);
        when(m_target.isWarnEnabled()).thenReturn(true);
        when(m_target.isErrorEnabled()).thenReturn(false);
        when(m_target.isTraceEnabled()).thenReturn(false);
        assertFalse(LogLevel.DISABLED.getIsEnabledFunction().apply(m_logger));
    }

    @Test
    public void testDisabled() {
        assertTrue(LogLevel.DISABLED.isDisabled());
        assertFalse(LogLevel.DEBUG.isDisabled());
    }

}