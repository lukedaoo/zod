package com.infamous.framework.logging.core;

public enum LogType {

    DEBUG,
    CUSTOMER;

    public String getType() {
        return this.name().toLowerCase();
    }
}
