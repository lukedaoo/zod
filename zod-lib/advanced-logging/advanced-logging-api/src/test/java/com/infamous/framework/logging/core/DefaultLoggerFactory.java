package com.infamous.framework.logging.core;

class DefaultLoggerFactory {

    public static TestDefaultLogger getLogger(String logCategory) {
        return new TestDefaultLogger(logCategory);
    }
}
