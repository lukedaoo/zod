package com.infamous.framework.logging.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.infamous.framework.sensitive.core.SensitiveObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Advanced Logger Test Suite")
class AdvancedLoggerTest {

    private org.slf4j.Logger m_internalLogger;
    private TestDefaultLogger m_wrapperLogger;

    @BeforeEach
    public void setup() {
        m_internalLogger = mock(org.slf4j.Logger.class);

        m_wrapperLogger = DefaultLoggerFactory.getLogger(AdvancedLoggerTest.class.getCanonicalName());

        m_wrapperLogger.setSlf4jLogger(m_internalLogger);
    }

    @Test
    public void testAllMethod() {
        String traceMessage = "wrapper trace message";
        String debugMessage = "wrapper debug message";
        String errorMessage = "wrapper error message";
        String infoMessage = "wrapper info message";
        String warningMessage = "wrapper warning message";

        Exception exception = new RuntimeException("exception");

        // Verify trace level
        m_wrapperLogger.trace(traceMessage);
        verify(m_internalLogger).trace(eq(traceMessage));

        m_wrapperLogger.trace(traceMessage, 1l);
        verify(m_internalLogger).trace(eq(traceMessage), eq(1l));

        m_wrapperLogger.trace(traceMessage, "a", "b");
        verify(m_internalLogger).trace(eq(traceMessage), eq("a"), eq("b"));

        m_wrapperLogger.trace(traceMessage, "a", "b", exception);
        verify(m_internalLogger).trace(eq(traceMessage), eq("a"), eq("b"), any(Exception.class));

        m_wrapperLogger.trace(traceMessage, exception);
        verify(m_internalLogger).trace(eq(traceMessage), eq(exception));

        m_wrapperLogger.isTraceEnabled();
        verify(m_internalLogger).isTraceEnabled();

        // Verify debug level
        m_wrapperLogger.debug(debugMessage);
        verify(m_internalLogger).debug(eq(debugMessage));

        m_wrapperLogger.debug(debugMessage, 1l);
        verify(m_internalLogger).debug(eq(debugMessage), eq(1l));

        m_wrapperLogger.debug(debugMessage, "a", "b");
        verify(m_internalLogger).debug(eq(debugMessage), eq("a"), eq("b"));

        m_wrapperLogger.debug(debugMessage, "a", "b", exception);
        verify(m_internalLogger).debug(eq(debugMessage), eq("a"), eq("b"), any(Exception.class));

        m_wrapperLogger.debug(debugMessage, exception);
        verify(m_internalLogger).debug(eq(debugMessage), eq(exception));

        m_wrapperLogger.isDebugEnabled();
        verify(m_internalLogger).isDebugEnabled();

        // Verify error level
        m_wrapperLogger.error(errorMessage);
        verify(m_internalLogger).error(eq(errorMessage));

        m_wrapperLogger.error(errorMessage, 1l);
        verify(m_internalLogger).error(eq(errorMessage), eq(1l));

        m_wrapperLogger.error(errorMessage, "a", "b");
        verify(m_internalLogger).error(eq(errorMessage), eq("a"), eq("b"));

        m_wrapperLogger.error(errorMessage, "a", "b", exception);
        verify(m_internalLogger).error(eq(errorMessage), eq("a"), eq("b"), any(Exception.class));

        m_wrapperLogger.error(errorMessage, exception);
        verify(m_internalLogger).error(eq(errorMessage), eq(exception));

        m_wrapperLogger.isErrorEnabled();
        verify(m_internalLogger).isErrorEnabled();

        // Verify info level
        m_wrapperLogger.info(infoMessage);
        verify(m_internalLogger).info(eq(infoMessage));

        m_wrapperLogger.info(infoMessage, 1l);
        verify(m_internalLogger).info(eq(infoMessage), eq(1l));

        m_wrapperLogger.info(infoMessage, "a", "b");
        verify(m_internalLogger).info(eq(infoMessage), eq("a"), eq("b"));

        m_wrapperLogger.info(infoMessage, "a", "b", exception);
        verify(m_internalLogger).info(eq(infoMessage), eq("a"), eq("b"), any(Exception.class));

        m_wrapperLogger.info(infoMessage, exception);
        verify(m_internalLogger).info(eq(infoMessage), eq(exception));

        m_wrapperLogger.isInfoEnabled();
        verify(m_internalLogger).isInfoEnabled();

        // Verify warn level
        m_wrapperLogger.warn(warningMessage);
        verify(m_internalLogger).warn(eq(warningMessage));

        m_wrapperLogger.warn(warningMessage, 1l);
        verify(m_internalLogger).warn(eq(warningMessage), eq(1l));

        m_wrapperLogger.warn(warningMessage, "a", "b");
        verify(m_internalLogger).warn(eq(warningMessage), eq("a"), eq("b"));

        m_wrapperLogger.warn(warningMessage, "a", "b", exception);
        verify(m_internalLogger).warn(eq(warningMessage), eq("a"), eq("b"), any(Exception.class));

        m_wrapperLogger.warn(warningMessage, exception);
        verify(m_internalLogger).warn(eq(warningMessage), eq(exception));

        m_wrapperLogger.isWarnEnabled();
        verify(m_internalLogger).isWarnEnabled();

        // Sensitive Object
        SensitiveObject sensitiveObjectWrapper = m_wrapperLogger.sensitiveObject("RTZ");

        assertNotNull(sensitiveObjectWrapper);
        assertEquals("RTZ", sensitiveObjectWrapper.replace());

        // Get key
        assertEquals(new LogKey("defaultLogger", "debug", "global"), m_wrapperLogger.getKey());
    }
}

