package com.infamous.framework.factory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.RawHttpResponse;
import com.infamous.framework.http.factory.ConverterFactory;
import java.lang.reflect.Type;

public class JacksonConverterFactory extends ConverterFactory {

    private final com.fasterxml.jackson.databind.ObjectMapper m_jacksonObjectMapper;
    private final ObjectMapper m_objectMapper;

    public static JacksonConverterFactory create() {
        return create(new com.fasterxml.jackson.databind.ObjectMapper());
    }

    public static JacksonConverterFactory create(com.fasterxml.jackson.databind.ObjectMapper mapper) {
        if (mapper == null) {
            throw new NullPointerException("mapper == null");
        }
        return new JacksonConverterFactory(mapper);
    }

    private JacksonConverterFactory(com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.m_jacksonObjectMapper = objectMapper;
        m_jacksonObjectMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        m_jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        m_objectMapper = new JacksonObjectMapper(m_jacksonObjectMapper);
    }

    @Override
    public ObjectMapper objectMapper() {
        return m_objectMapper;
    }

    @Override
    public <T> Converter<T, BodyPart> requestBodyConverter(Type type) {
        JavaType javaType = m_jacksonObjectMapper.getTypeFactory().constructType(type);
        ObjectWriter writer = m_jacksonObjectMapper.writerFor(javaType);
        return new JacksonRequestBodyConverter<>(writer);
    }

    @Override
    public <T> Converter<RawHttpResponse, T> responseBodyConverter(Type type) {
        JavaType javaType = m_jacksonObjectMapper.getTypeFactory().constructType(type);
        ObjectReader reader = m_jacksonObjectMapper.readerFor(javaType);
        return new JacksonResponseBodyConverter<>(reader);
    }

    private static final class JacksonObjectMapper implements ObjectMapper {

        private final com.fasterxml.jackson.databind.ObjectMapper m_internalObjectMapper;

        public JacksonObjectMapper(com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
            m_internalObjectMapper = objectMapper;
        }

        @Override
        public <T> T readValue(String value, Class<T> valueType) throws RuntimeException {
            try {
                return m_internalObjectMapper.readValue(value, valueType);
            } catch (Exception e) {
                throw new ZodHttpException(e);
            }
        }

        @Override
        public String writeValue(Object value) throws RuntimeException {
            try {
                return m_internalObjectMapper.writeValueAsString(value);
            } catch (Exception e) {
                throw new ZodHttpException(e);
            }
        }
    }
}
