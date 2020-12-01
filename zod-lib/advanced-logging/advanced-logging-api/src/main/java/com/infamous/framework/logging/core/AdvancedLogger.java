package com.infamous.framework.logging.core;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.core.SensitiveObject;

public interface AdvancedLogger {


    String getName();

    LogKey getKey();

    // -- Trace Level

    Boolean isTraceEnabled();

    void trace(String msg);

    void trace(String format, Object arg);

    void trace(String format, Object arg1, Object arg2);

    void trace(String format, Object... arguments);

    void trace(String msg, Throwable t);

    // -- Debug Level

    Boolean isDebugEnabled();

    void debug(String msg);

    void debug(String format, Object arg);

    void debug(String format, Object arg1, Object arg2);

    void debug(String format, Object... arguments);

    void debug(String msg, Throwable t);

    // -- Info Level

    Boolean isInfoEnabled();

    void info(String msg);

    void info(String format, Object arg);

    void info(String format, Object arg1, Object arg2);

    void info(String format, Object... arguments);

    void info(String msg, Throwable t);

    // -- Warning Level

    Boolean isWarnEnabled();

    void warn(String msg);

    void warn(String format, Object arg);

    void warn(String format, Object... arguments);

    void warn(String format, Object arg1, Object arg2);

    void warn(String msg, Throwable t);

    // -- Error Level

    Boolean isErrorEnabled();

    void error(String msg);

    void error(String format, Object arg);

    void error(String format, Object arg1, Object arg2);

    void error(String format, Object... arguments);

    void error(String msg, Throwable t);

    SensitiveObject sensitiveObject(Object obj);

    SensitiveObject sensitiveObject(Object obj, MessageDigestAlgorithm algorithm);

    org.slf4j.Logger getTarget();
}
