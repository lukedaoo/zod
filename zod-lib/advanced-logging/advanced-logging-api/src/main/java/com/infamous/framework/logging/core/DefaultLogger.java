package com.infamous.framework.logging.core;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.core.SensitiveObject;
import org.slf4j.Logger;

public class DefaultLogger implements AdvancedLogger {

    private Logger m_slf4jLogger;

    public DefaultLogger(Class<?> clazz) {
        m_slf4jLogger = org.slf4j.LoggerFactory.getLogger(clazz);
    }

    public DefaultLogger(String category) {
        m_slf4jLogger = org.slf4j.LoggerFactory.getLogger(category);
    }

    @Override
    public String getName() {
        return m_slf4jLogger.getName();
    }

    @Override
    public LogKey getKey() {
        return null;
    }

    @Override
    public Boolean isTraceEnabled() {
        return m_slf4jLogger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        m_slf4jLogger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        m_slf4jLogger.trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        m_slf4jLogger.trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        m_slf4jLogger.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        m_slf4jLogger.trace(msg, t);
    }

    @Override
    public Boolean isDebugEnabled() {
        return m_slf4jLogger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        m_slf4jLogger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        m_slf4jLogger.debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        m_slf4jLogger.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        m_slf4jLogger.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        m_slf4jLogger.debug(msg, t);
    }

    @Override
    public Boolean isInfoEnabled() {
        return m_slf4jLogger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        m_slf4jLogger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        m_slf4jLogger.info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        m_slf4jLogger.info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        m_slf4jLogger.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        m_slf4jLogger.info(msg, t);
    }

    @Override
    public Boolean isWarnEnabled() {
        return m_slf4jLogger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        m_slf4jLogger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        m_slf4jLogger.warn(format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        m_slf4jLogger.warn(format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        m_slf4jLogger.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        m_slf4jLogger.warn(msg, t);
    }

    @Override
    public Boolean isErrorEnabled() {
        return m_slf4jLogger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        m_slf4jLogger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        m_slf4jLogger.error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        m_slf4jLogger.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        m_slf4jLogger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        m_slf4jLogger.error(msg, t);
    }

    @Override
    public SensitiveObject sensitiveObject(Object obj) {
        return () -> String.valueOf(obj);
    }

    @Override
    public SensitiveObject sensitiveObject(Object obj, MessageDigestAlgorithm algorithm) {
        return () -> String.valueOf(obj);
    }

    @Override
    public Logger getTarget() {
        return m_slf4jLogger;
    }

    // For UT
    protected void setSlf4jLogger(Logger logger) {
        m_slf4jLogger = logger;
    }
}
