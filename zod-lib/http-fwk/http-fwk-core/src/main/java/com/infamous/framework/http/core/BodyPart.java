package com.infamous.framework.http.core;

public abstract class BodyPart<T> implements Comparable {

    private final String m_name;
    private final T m_value;
    private final String m_contentType;
    private final Class<?> m_partType;

    protected BodyPart(T value, String name, String contentType) {
        this.m_name = name;
        this.m_value = value;
        this.m_contentType = contentType;
        this.m_partType = value.getClass();
    }

    public String getName() {
        return m_name;
    }

    public T getValue() {
        return m_value;
    }

    public String getContentType() {
        if (m_contentType == null || m_contentType.isEmpty()) {
            if (isFile()) {
                return "application/octet-stream";
            }
            return "application/x-www-form-urlencoded";
        }
        return m_contentType;
    }

    public Class<?> getPartType() {
        return m_partType;
    }

    public String getFileName() {
        return null;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof BodyPart) {
            return getName().compareTo(((BodyPart) o).getName());
        }
        return 0;
    }

    abstract public boolean isFile();
}
