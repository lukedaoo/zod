package com.infamous.framework.logging.core;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public enum LogLevel {
    DEBUG("DEBUG", CheckLogEnabledHelper::isDebugEnabled, LogInvoke::debug),
    INFO("INFO", CheckLogEnabledHelper::isInfoEnabled, LogInvoke::info),
    WARNING("WARNING", CheckLogEnabledHelper::isWarningEnabled, LogInvoke::warning),
    ERROR("ERROR", CheckLogEnabledHelper::isErrorEnabled, LogInvoke::error),
    TRACE("TRACE", CheckLogEnabledHelper::isTraceEnabled, LogInvoke::trace),
    DISABLED("DISABLED", CheckLogEnabledHelper::isDisabled, (logger, message) -> {
        throw new IllegalStateException(logger.getName() + " is disabled");
    });

    private final String m_level;
    private final Function<AdvancedLogger, Boolean> m_isEnabledFunction;

    LogLevel(String level, Function<AdvancedLogger, Boolean> isEnabledFunction,
             BiConsumer<AdvancedLogger, LogMessage> consumer) {
        this.m_level = level;
        this.m_consumerInvokeLog = consumer;
        this.m_isEnabledFunction = isEnabledFunction;
    }

    public BiConsumer<AdvancedLogger, LogMessage> getConsumerInvokeLog() {
        return m_consumerInvokeLog;
    }

    public Function<AdvancedLogger, Boolean> getIsEnabledFunction() {
        return m_isEnabledFunction;
    }

    public static LogLevel fromLevel(final Optional<String> level) {
        if (level.isPresent()) {
            return Arrays.stream(LogLevel.values())
                .filter(logLevel -> logLevel.getLevel().equalsIgnoreCase(level.get()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unexpected level"));
        }
        throw new IllegalArgumentException("Unexpected level");
    }

    public static boolean isSupportedLevel(final Optional<String> level) {

        return fromLevel(level) != null;
    }

    public String getLevel() {
        return m_level;
    }

    public boolean isDisabled() {
        return this.equals(DISABLED);
    }

    private static class CheckLogEnabledHelper {

        public static boolean isDebugEnabled(AdvancedLogger logger) {
            return logger.isDebugEnabled();
        }

        public static boolean isInfoEnabled(AdvancedLogger logger) {
            return logger.isInfoEnabled();
        }

        public static boolean isWarningEnabled(AdvancedLogger logger) {
            return logger.isWarnEnabled();
        }

        public static boolean isErrorEnabled(AdvancedLogger logger) {
            return logger.isErrorEnabled();
        }

        public static boolean isTraceEnabled(AdvancedLogger logger) {
            return logger.isTraceEnabled();
        }

        public static boolean isDisabled(AdvancedLogger logger) {
            return !isDebugEnabled(logger)
                && !isInfoEnabled(logger)
                && !isWarningEnabled(logger)
                && !isErrorEnabled(logger)
                && !isTraceEnabled(logger);
        }
    }

    private static class LogInvoke {

        public static void debug(AdvancedLogger logger, LogMessage message) {
            if (message.getArguments() == null) {
                logger.debug(message.getMessage());
                return;
            }
            logger.debug(message.getMessage(), message.getArguments());
        }

        public static void info(AdvancedLogger logger, LogMessage message) {
            if (message.getArguments() == null) {
                logger.info(message.getMessage());
                return;
            }
            logger.info(message.getMessage(), message.getArguments());
        }

        public static void warning(AdvancedLogger logger, LogMessage message) {
            if (message.getArguments() == null) {
                logger.warn(message.getMessage());
                return;
            }
            logger.warn(message.getMessage(), message.getArguments());
        }

        public static void error(AdvancedLogger logger, LogMessage message) {
            if (message.getArguments() == null) {
                logger.error(message.getMessage());
                return;
            }
            logger.error(message.getMessage(), message.getArguments());
        }

        public static void trace(AdvancedLogger logger, LogMessage message) {
            if (message.getArguments() == null) {
                logger.trace(message.getMessage());
                return;
            }
            logger.trace(message.getMessage(), message.getArguments());
        }
    }
}
