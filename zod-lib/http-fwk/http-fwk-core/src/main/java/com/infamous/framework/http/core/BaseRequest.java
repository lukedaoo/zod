package com.infamous.framework.http.core;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.ZodHttpException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

abstract class BaseRequest<R extends HttpRequest<?>> implements HttpRequest<R> {

    private Path m_path;
    private HeaderMap m_header;
    private HttpMethod m_method;

    private Optional<ObjectMapper> m_objectMapper = Optional.empty();

    BaseRequest(String baseUrl, String url, HttpMethod method, ObjectMapper objectMapper) {
        m_path = new Path(baseUrl, url);
        m_header = new HeaderMap();
        m_method = method;
        withObjectMapper(objectMapper);
    }

    BaseRequest(BaseRequest request) {
        m_path = request.m_path;
        m_header = request.m_header;
        m_method = request.m_method;
        withObjectMapper(request.getObjectMapper());
    }

    @Override
    public R queryParam(String key, Collection<?> list, boolean encoded) {
        m_path.appendQueryParam(key, list, encoded);
        return (R) this;
    }

    @Override
    public R queryParam(String key, Object value, boolean encoded) {
        m_path.appendQueryParam(key, value, encoded);
        return (R) this;
    }

    @Override
    public R pathParam(String key, String value, boolean encoded) {
        m_path.appendPathParam(key, value, encoded);
        return (R) this;
    }


    @Override
    public R header(String key, String value) {
        m_header.add(key, value);
        return (R) this;
    }

    @Override
    public R headers(Map<String, String> headerMap) {
        m_header.add(headerMap);
        return (R) this;
    }

    @Override
    public R appendUrl(String url) {
        m_path.appendUrl(url);
        return (R) this;
    }

    @Override
    public R useUrl(String url) {
        m_path.useUrl(url);
        return (R) this;
    }

    @Override
    public String getUrl() {
        return m_path.getUrl();
    }

    @Override
    public HttpMethod getMethod() {
        return m_method;
    }

    @Override
    public Map<String, String> getHeaders() {
        return m_header.get();
    }

    @Override
    public R withObjectMapper(ObjectMapper mapper) {
        Objects.requireNonNull(mapper, "ObjectMapper may not be null");
        this.m_objectMapper = Optional.of(mapper);
        return (R) this;
    }

    protected ObjectMapper getObjectMapper() {
        return m_objectMapper.orElseThrow(() -> new ZodHttpException("Not found ObjectMapper"));
    }
}
