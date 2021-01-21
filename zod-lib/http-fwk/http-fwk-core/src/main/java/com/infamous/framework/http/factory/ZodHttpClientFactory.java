package com.infamous.framework.http.factory;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.RestClient;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.RawHttpResponse;
import com.infamous.framework.http.engine.CallEngine;
import com.infamous.framework.logging.core.AdvancedLogger;
import java.lang.reflect.Type;
import java.util.Objects;

public final class ZodHttpClientFactory {

    private final String m_baseUrl;
    private final AdvancedLogger m_logger;
    private final HttpConfig m_httpConfig;
    private final ConverterFactory m_converterFactory;
    private final CallEngine m_callEngine;

    public ZodHttpClientFactory(Builder builder) {
        Objects.requireNonNull(builder.m_httpConfig, "Config == null");
        Objects.requireNonNull(builder.m_converterFactory, "ConverterFactory == null");
        Objects.requireNonNull(builder.m_converterFactory.getObjectMapper(), "ObjectMapper == null");
        Objects.requireNonNull(builder.m_calEngine, "CallEngine == null");

        this.m_baseUrl = builder.m_baseUrl;
        this.m_logger = builder.m_logger;
        this.m_httpConfig = builder.m_httpConfig;

        this.m_converterFactory = builder.m_converterFactory;
        this.m_callEngine = builder.m_calEngine;
    }

    public <ServiceType> ServiceType create(Class<ServiceType> service) {
        validateServiceInterface(service);
        return ServiceProxy.create(this, service);
    }

    public String baseUrl() {
        return m_baseUrl;
    }

    public AdvancedLogger logger() {
        return m_logger;
    }

    public HttpConfig config() {
        return m_httpConfig;
    }

    private <T> void validateServiceInterface(Class<T> service) {
        if (service == null) {
            throw new NullPointerException("API interface must be not NULL");
        }
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        if (Objects.isNull(service.getAnnotation(RestClient.class))) {
            throw new IllegalArgumentException("Only support for type that has @RestClient annotation");
        }
    }

    Converter<Object, String> stringConverter() {
        return BuiltInConverterFactory.ToStringConverter.getInstance();
    }

    ObjectMapper getObjectMapper() {
        return m_converterFactory.getObjectMapper();
    }

    public CallEngine getCallEngine() {
        return m_callEngine;
    }

    public static Builder builder() {
        return new Builder();
    }

    public <T> Converter<T, BodyPart> requestBodyConverter(Type type) {
        return m_converterFactory.requestBodyConverter(type);
    }

    public <T> Converter<RawHttpResponse, T> responseBodyConverter(Type type) {
        return m_converterFactory.responseBodyConverter(type);
    }

    public static final class Builder {

        public CallEngine m_calEngine;
        private String m_baseUrl;
        private AdvancedLogger m_logger;
        private ConverterFactory m_converterFactory;
        private HttpConfig m_httpConfig;

        public Builder() {

        }

        public Builder baseUrl(String baseUrl) {
            m_baseUrl = baseUrl;
            return this;
        }

        public Builder logger(AdvancedLogger logger) {
            m_logger = logger;
            return this;
        }

        public Builder config(HttpConfig config) {
            m_httpConfig = config;
            return this;
        }

        public Builder converterFactory(ConverterFactory converterFactory) {
            m_converterFactory = converterFactory;
            return this;
        }

        public Builder callEngine(CallEngine callEngine) {
            m_calEngine = callEngine;
            return this;
        }

        public ZodHttpClientFactory build() {
            return new ZodHttpClientFactory(this);
        }
    }
}
