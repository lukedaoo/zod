package com.infamous.framework.logging.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.infamous.framework.logging.core.DefaultLogger;
import com.infamous.framework.logging.core.LogKey;
import com.infamous.framework.logging.core.LogLevel;
import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZodLoggerImplTest {


    private ZodLoggerImpl m_zodLogger;
    private DefaultLogger m_target;

    @BeforeEach
    public void setup() {
        m_target = spy(new DefaultLogger(ZodLoggerImplTest.class));
        m_zodLogger = new ZodLoggerImpl(
            new LogKey("logging", "debug", "global"), m_target);
    }

    @Test
    public void testLogInfo() {
        m_zodLogger.info("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verifyNeverInvokeCheckingOperator();
        verify(m_target).info(anyString());
    }

    @Test
    public void testLogDebug() {
        m_zodLogger.debug("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verifyNeverInvokeCheckingOperator();
        verify(m_target).debug(anyString());
    }


    @Test
    public void testLogWarning() {
        m_zodLogger.warn("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verifyNeverInvokeCheckingOperator();
        verify(m_target).warn(anyString());
    }


    @Test
    public void testLogError() {
        m_zodLogger.error("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verifyNeverInvokeCheckingOperator();
        verify(m_target).error(anyString());
    }

    @Test
    public void testLogTrace() {
        m_zodLogger.trace("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verifyNeverInvokeCheckingOperator();
        verify(m_target).trace(anyString());
    }

    @Test
    public void testFastLog_Debug() {
        m_zodLogger.fastLog(LogLevel.DEBUG, "Message");
        verify(m_target).isDebugEnabled();
        verify(m_target).debug(anyString());
    }

    @Test
    public void testFastLog_Info() {
        m_zodLogger.fastLog(LogLevel.INFO, "Message");
        verify(m_target).isInfoEnabled();
        verify(m_target).info(anyString());
    }

    @Test
    public void testFastLog_Warn() {
        m_zodLogger.fastLog(LogLevel.WARNING, "Message");
        verify(m_target).isWarnEnabled();
        verify(m_target).warn(anyString());
    }

    @Test
    public void testFastLog_Error() {
        m_zodLogger.fastLog(LogLevel.ERROR, "Message");
        verify(m_target).isErrorEnabled();
        verify(m_target).error(anyString());
    }

    @Test
    public void testFastLog_Trace() {
        m_zodLogger.fastLog(LogLevel.TRACE, "Message");
        verify(m_target).isTraceEnabled();
        verify(m_target).trace(anyString());
    }

    private void verifyNeverInvokeCheckingOperator() {
        verify(m_target, never()).isTraceEnabled();
        verify(m_target, never()).isDebugEnabled();
        verify(m_target, never()).isInfoEnabled();
        verify(m_target, never()).isWarnEnabled();
        verify(m_target, never()).isErrorEnabled();
    }
}