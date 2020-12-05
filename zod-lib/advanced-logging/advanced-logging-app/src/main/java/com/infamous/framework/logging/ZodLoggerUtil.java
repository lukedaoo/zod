package com.infamous.framework.logging;

import com.infamous.framework.logging.core.LogScope;
import com.infamous.framework.logging.core.LogType;

public class ZodLoggerUtil {

    public static ZodLogger getLogger(String category, String applicationName) {
        return ZodLoggerFactory.getInstance().getLogger(category, applicationName,
            LogType.DEBUG.getType(), LogScope.GLOBAL.getScope());
    }

    public static ZodLogger getLogger(Class<?> clazz, String applicationName) {
        return ZodLoggerFactory.getInstance().getLogger(clazz, applicationName,
            LogType.DEBUG.getType(), LogScope.GLOBAL.getScope());
    }
}
