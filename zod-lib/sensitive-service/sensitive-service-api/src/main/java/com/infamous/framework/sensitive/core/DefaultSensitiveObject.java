package com.infamous.framework.sensitive.core;

public class DefaultSensitiveObject implements SensitiveObject {

    private final Object m_object;

    public DefaultSensitiveObject(Object object) {
        this.m_object = object;
    }

    public Object getObject() {
        return m_object;
    }

    @Override
    public String toString() {
        return replace();
    }

    @Override
    public String replace() {
        return MessageDigestAlgorithm.MD5.hash(String.valueOf(m_object));
    }
}
