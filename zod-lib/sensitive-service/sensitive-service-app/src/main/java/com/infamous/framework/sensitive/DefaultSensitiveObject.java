package com.infamous.framework.sensitive;

import com.infamous.framework.sensitive.core.SensitiveObject;

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
        return String.valueOf(m_object);
    }
}
