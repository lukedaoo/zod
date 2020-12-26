package com.infamous.framework.logging.core;

public enum LogScope {

    GLOBAL,
    INTERNAL;

    public String getScope() {
        return this.name().toLowerCase();
    }
}
