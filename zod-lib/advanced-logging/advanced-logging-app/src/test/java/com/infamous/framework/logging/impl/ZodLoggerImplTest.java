package com.infamous.framework.logging.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.infamous.framework.logging.core.DefaultLogger;
import com.infamous.framework.logging.core.LogKey;
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

        verify(m_target).info(anyString());
    }


    @Test
    public void testLogDebug() {
        m_zodLogger.debug("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verify(m_target).debug(anyString());
    }


    @Test
    public void testLogWarning() {
        m_zodLogger.warn("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verify(m_target).warn(anyString());
    }


    @Test
    public void testLogError() {
        m_zodLogger.error("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verify(m_target).error(anyString());
    }


    @Test
    public void testLogTrace() {
        m_zodLogger.trace("ABC" + m_zodLogger.sensitiveObject("OK", MessageDigestAlgorithm.MD5));

        verify(m_target).trace(anyString());
    }
}