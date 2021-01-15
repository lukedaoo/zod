package com.infamous.framework.http.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class HeaderMap {

    private Map<String, String> m_headers;

    public HeaderMap() {
        m_headers = new HashMap<>();
    }

    public HeaderMap(Map<String, String> map) {
        m_headers = map;
    }

    public void add(String key, String value) {
        m_headers.put(key, value);
    }

    public void add(Map<String, String> headers) {
        m_headers.putAll(headers);
    }

    public String get(String key) {
        return m_headers.get(key);
    }

    public void remove(String key) {
        m_headers.remove(key);
    }

    public Map<String, String> get() {
        return m_headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeaderMap headerMap = (HeaderMap) o;
        return Objects.equals(m_headers, headerMap.m_headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_headers);
    }
}
