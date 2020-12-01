package com.infamous.framework.logging.config;

import com.infamous.framework.logging.core.LogKey;
import com.infamous.framework.logging.core.LogLevel;
import java.util.Map;

public interface AdvancedLoggingConfiguration {

    void changeLevel(Class<?> category, String level);

    void changeLevel(String category, String level);

    void changeLevel(LogKey key, LogLevel logLevel);

    void changeLevel(Map<LogKey, LogLevel> logKeyLogLevelMap);
}
