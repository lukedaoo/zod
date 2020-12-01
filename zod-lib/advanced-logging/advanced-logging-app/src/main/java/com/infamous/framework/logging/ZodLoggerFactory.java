package com.infamous.framework.logging;

import com.infamous.framework.logging.impl.ZodLoggerImpl;
import com.infamous.framework.logging.core.AdvancedLogger;
import com.infamous.framework.logging.core.AdvancedLoggerFactory;
import com.infamous.framework.logging.core.ApplicationName;
import com.infamous.framework.logging.core.DefaultLogger;
import com.infamous.framework.logging.core.LogKey;
import com.infamous.framework.logging.core.LogScope;
import com.infamous.framework.logging.core.LogType;
import com.infamous.framework.sensitive.core.SensitiveHashingService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ZodLoggerFactory implements AdvancedLoggerFactory<ZodLogger> {

    public static final String PREFIX_CATEGORY = "com.infamous";

    private static Map<LogKey, Set<String>> CATEGORIES = new ConcurrentHashMap<>();
    private static Map<String, LogKey> CATEGORIES_TO_LOGGER_KEY = new ConcurrentHashMap<>();
    private static SensitiveHashingService SENSITIVE_DATA_SERVICE = new ZodSensitiveHashingService();

    private static ZodLoggerFactory INSTANCE = new ZodLoggerFactory();

    public static ZodLoggerFactory getInstance() {
        return INSTANCE;
    }

    public void setSensitiveDataService(SensitiveHashingService hashingService) {
        SENSITIVE_DATA_SERVICE = hashingService;
    }

    public Optional<SensitiveHashingService> getSensitiveHashingService() {
        return Optional.ofNullable(SENSITIVE_DATA_SERVICE);
    }

    @Override
    public ZodLogger getLogger(String category, ApplicationName applicationName, LogType logType,
                               LogScope logScope) {

        return getLogger(category, applicationName.getName(), logType.getType(), logScope.getScope());
    }

    @Override
    public ZodLogger getLogger(Class<?> clazz, String applicationName, String logType, String logScope) {

        return getLogger(clazz.getName(), applicationName, logType, logScope);
    }

    @Override
    public ZodLogger getLogger(String category, String applicationName, String logType, String logScope) {
        if (!category.startsWith(PREFIX_CATEGORY)) {
            throw new IllegalArgumentException("Only support category start with " + PREFIX_CATEGORY);
        }
        LogKey loggerKey = new LogKey(applicationName, logType, logScope);
        category = getUniqueCategory(category, loggerKey);

        Set<String> categories = CATEGORIES.get(loggerKey);

        if (categories == null) {
            categories = new HashSet<>();
            CATEGORIES.put(loggerKey, categories);
            categories = CATEGORIES.get(loggerKey);
        }

        categories.add(category);
        CATEGORIES.putIfAbsent(loggerKey, categories);

        AdvancedLogger logger = getInternalLogger(category);

        return new ZodLoggerImpl(loggerKey, logger);
    }

    private String getUniqueCategory(String category, LogKey key) {
        LogKey loggerKey = CATEGORIES_TO_LOGGER_KEY.get(category);
        if (loggerKey != null && !loggerKey.equals(key)) {
            synchronized (ZodLoggerFactory.class) {
                int i = 0;
                String newCategory = category;
                LogKey loggerKeyWithNewCategory = loggerKey;
                while (loggerKeyWithNewCategory != null && !loggerKeyWithNewCategory.equals(key)) {
                    newCategory = category + "#" + i;
                    loggerKeyWithNewCategory = CATEGORIES_TO_LOGGER_KEY.get(newCategory);
                    i++;
                }
                category = newCategory;
            }
        }
        CATEGORIES_TO_LOGGER_KEY.putIfAbsent(category, key);
        return category;
    }

    public Set<String> getCategoriesByLogKey(LogKey logKey) {
        Set<String> categories = CATEGORIES.get(logKey);

        return categories != null ? new HashSet<>(categories) : Collections.emptySet();
    }

    public Set<LogKey> getLogKeyRegister() {
        return new HashSet<>(CATEGORIES.keySet());
    }

    private AdvancedLogger getInternalLogger(String category) {
        return new DefaultLogger(category);
    }
}

