package com.infamous.framework.converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GenericType<T> {

    protected final Type m_type;

    protected GenericType() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException(
                "Internal error: TypeReference constructed without actual type information");
        } else {
            this.m_type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }

    public Type getType() {
        return this.m_type;
    }

    public Class<?> getTypeClass() {
        return this.m_type.getClass();
    }
}
