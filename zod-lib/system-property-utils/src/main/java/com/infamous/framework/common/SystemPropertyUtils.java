package com.infamous.framework.common;

import java.util.function.Function;

public class SystemPropertyUtils {

    private static SystemPropertyUtils INSTANCE = new SystemPropertyUtils();

    private SystemPropertyUtils() {
    }

    public static SystemPropertyUtils getInstance() {
        return INSTANCE;
    }

    public String getProperty(String propKey, String defaultValue) {
        String res = System.getenv(propKey);
        if (res == null) {
            res = System.getProperty(propKey);
        }
        if (res == null && defaultValue != null) {
            res = defaultValue;
        }
        return res;
    }

    public int getAsInt(String prop, int defaultValue) {
        return getAs(prop, defaultValue, Integer::parseInt);
    }

    public long getAsLong(String prop, long defaultValue) {
        return getAs(prop, defaultValue, Long::parseLong);

    }

    public boolean getAsBoolean(String prop, boolean defaultValue) {
        return getAs(prop, defaultValue, s -> {
            if ("true".equalsIgnoreCase(s)) {
                return true;
            } else if ("false".equalsIgnoreCase(s)) {
                return false;
            } else {
                throw new IllegalArgumentException("Can not parse " + s + " to boolean");
            }
        });
    }

    private <T> T getAs(String prop, T defaultValue, Function<String, T> convertFunction) {
        try {
            String value = getProperty(prop, String.valueOf(defaultValue));
            return convertFunction.apply(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
