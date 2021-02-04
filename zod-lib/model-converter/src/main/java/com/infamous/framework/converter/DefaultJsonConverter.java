package com.infamous.framework.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

public class DefaultJsonConverter implements ObjectMapper {

    private com.fasterxml.jackson.databind.ObjectMapper m_jacksonObjectMapper;

    private static DefaultJsonConverter INSTANCE;

    private DefaultJsonConverter() {
        m_jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    public synchronized static DefaultJsonConverter getInstance() {
        if (INSTANCE == null) {
            synchronized (DefaultJsonConverter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DefaultJsonConverter();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public <T> T readValue(String value, Class<T> valueType) throws RuntimeException {
        try {
            return m_jacksonObjectMapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String writeValue(Object value) throws RuntimeException {
        try {
            return m_jacksonObjectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T readValue(String value, TypeReference<T> type) {
        try {
            return m_jacksonObjectMapper.readValue(value, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public com.fasterxml.jackson.databind.ObjectMapper getJacksonObjectMapper() {
        return m_jacksonObjectMapper;
    }
}
