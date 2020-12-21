package com.infamous.framework.logging;

import com.infamous.framework.logging.core.AdvancedLogger;
import com.infamous.framework.logging.core.LogLevel;

public interface ZodLogger extends AdvancedLogger {

    void fastLog(LogLevel level, String msg);

    void fastLog(LogLevel level, String format, Object arg);

    void fastLog(LogLevel level, String format, Object arg1, Object arg2);

    void fastLog(LogLevel level, String format, Object... arguments);

    void fastLog(LogLevel level, String msg, Throwable t);
}
