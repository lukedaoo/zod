package com.infamous.framework.logging.core;

public interface AdvancedLoggerFactory<LOG extends AdvancedLogger> {

    LOG getLogger(Class<?> clazz, ApplicationName applicationName, LogType logType,
                  LogScope logScope);

    LOG getLogger(String category, ApplicationName applicationName,
                  LogType logType, LogScope logScope);

    LOG getLogger(Class<?> clazz, String applicationName, String logType,
                  String logScope);

    LOG getLogger(String category, String applicationName, String logType,
                  String logScope);
}
