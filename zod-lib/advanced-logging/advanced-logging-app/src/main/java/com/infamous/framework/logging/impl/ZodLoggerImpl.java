package com.infamous.framework.logging.impl;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodSensitiveObject;
import com.infamous.framework.logging.core.AdvancedLogger;
import com.infamous.framework.logging.core.LogKey;
import com.infamous.framework.logging.core.LogLevel;
import com.infamous.framework.logging.core.LogMessage;
import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.core.SensitiveObject;
import org.slf4j.Logger;
import org.slf4j.MDC;

public class ZodLoggerImpl implements ZodLogger {

    private static final String APPLICATION = "application";
    private static final String SCOPE = "scope";
    private static final String TYPE = "type";

    private final LogKey m_logKey;
    private final AdvancedLogger m_target;

    public ZodLoggerImpl(LogKey logKey, AdvancedLogger target) {
        this.m_logKey = logKey;
        this.m_target = target;
    }

    @Override
    public String getName() {
        return m_logKey.getApplicationName();
    }

    @Override
    public LogKey getKey() {
        return m_logKey;
    }

    @Override
    public Boolean isTraceEnabled() {
        return isOperationEnabled(LogLevel.TRACE);
    }

    @Override
    public void trace(String msg) {
        log(LogLevel.TRACE, msg);
    }

    @Override
    public void trace(String format, Object arg) {
        log(LogLevel.TRACE, format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(LogLevel.TRACE, format, new Object[]{arg1, arg2});
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(LogLevel.TRACE, format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(LogLevel.TRACE, msg, t);
    }

    @Override
    public Boolean isDebugEnabled() {
        return isOperationEnabled(LogLevel.DEBUG);
    }

    @Override
    public void debug(String msg) {
        log(LogLevel.DEBUG, msg);
    }

    @Override
    public void debug(String format, Object arg) {
        log(LogLevel.DEBUG, format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(LogLevel.DEBUG, format, new Object[]{arg1, arg2});
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(LogLevel.DEBUG, format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(LogLevel.DEBUG, msg, t);
    }

    @Override
    public Boolean isInfoEnabled() {
        return isOperationEnabled(LogLevel.INFO);
    }

    @Override
    public void info(String msg) {
        log(LogLevel.INFO, msg);
    }

    @Override
    public void info(String format, Object arg) {
        log(LogLevel.INFO, format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        log(LogLevel.INFO, format, new Object[]{arg1, arg2});
    }

    @Override
    public void info(String format, Object... arguments) {
        log(LogLevel.INFO, format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(LogLevel.INFO, msg, t);
    }

    @Override
    public Boolean isWarnEnabled() {
        return isOperationEnabled(LogLevel.WARNING);
    }

    @Override
    public void warn(String msg) {
        log(LogLevel.WARNING, msg);
    }

    @Override
    public void warn(String format, Object arg) {
        log(LogLevel.WARNING, format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(LogLevel.WARNING, format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(LogLevel.WARNING, format, new Object[]{arg1, arg2});
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(LogLevel.WARNING, msg, t);
    }

    @Override
    public Boolean isErrorEnabled() {
        return isOperationEnabled(LogLevel.ERROR);
    }

    @Override
    public void error(String msg) {
        log(LogLevel.ERROR, msg);
    }

    @Override
    public void error(String format, Object arg) {
        log(LogLevel.ERROR, format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(LogLevel.ERROR, format, new Object[]{arg1, arg2});
    }

    @Override
    public void error(String format, Object... arguments) {
        log(LogLevel.ERROR, format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(LogLevel.ERROR, msg, t);
    }

    @Override
    public SensitiveObject sensitiveObject(Object obj) {
        return new ZodSensitiveObject(obj);
    }

    @Override
    public SensitiveObject sensitiveObject(Object obj, MessageDigestAlgorithm algorithm) {
        return new ZodSensitiveObject(obj, algorithm);
    }

    @Override
    public Logger getTarget() {
        return m_target.getTarget();
    }

    @Override
    public void fastLog(LogLevel level, String msg) {
        if (isOperationEnabled(level)) {
            log(level, msg);
        }
    }

    @Override
    public void fastLog(LogLevel level, String format, Object arg) {
        if (isOperationEnabled(level)) {
            log(level, format, arg);
        }
    }

    @Override
    public void fastLog(LogLevel level, String format, Object arg1, Object arg2) {
        if (isOperationEnabled(level)) {
            log(level, format, new Object[]{arg1, arg2});
        }
    }

    @Override
    public void fastLog(LogLevel level, String format, Object... arguments) {
        if (isOperationEnabled(level)) {
            log(level, format, arguments);
        }
    }

    @Override
    public void fastLog(LogLevel level, String msg, Throwable t) {
        if (isOperationEnabled(level)) {
            log(level, msg);
        }
    }


    private boolean isOperationEnabled(LogLevel logLevel) {
        return logLevel.getIsEnabledFunction().apply(m_target);
    }

    private void setMDC() {
        MDC.put(APPLICATION, m_logKey.getApplicationName());
        MDC.put(SCOPE, m_logKey.getLogScope());
        MDC.put(TYPE, m_logKey.getLogType());
    }

    private void resetMDC() {
        MDC.remove(APPLICATION);
        MDC.remove(SCOPE);
        MDC.remove(TYPE);
    }

    private String prependDetail(String message) {
        StringBuilder builder = new StringBuilder();

        builder.append("[");
        builder.append(" ").append(APPLICATION).append("=").append(m_logKey.getApplicationName());
        builder.append(", ").append(TYPE).append("=").append(m_logKey.getLogType());
        builder.append(", ").append(SCOPE).append("=").append(m_logKey.getLogScope());
        builder.append(" ]");
        builder.append(" ").append(message);

        return builder.toString();
    }

    private void log(LogLevel logLevel, String rawMessage, Object object) {
        log(logLevel, rawMessage, new Object[]{object});
    }

    private void log(LogLevel logLevel, String rawMessage) {
        log(logLevel, rawMessage, null);
    }

    private void log(LogLevel logLevel, String rawMessage, Object[] objects) {
        String message = prependDetail(rawMessage);
        try {
            // setMDC();
            invokeLog(logLevel, message, objects);
        } finally {
            resetMDC();
        }
    }

    private void invokeLog(LogLevel level, String message, Object[] args) {
        level.getConsumerInvokeLog().accept(m_target, new LogMessage(message, args));
    }
}
