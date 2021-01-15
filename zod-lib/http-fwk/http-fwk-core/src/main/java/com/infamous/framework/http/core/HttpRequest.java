package com.infamous.framework.http.core;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpMethod;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface HttpRequest<R extends HttpRequest> {

    R queryParam(String key, Collection<?> list, boolean encoded);

    default R queryParam(String key, Collection<?> list) {
        return queryParam(key, list, false);
    }

    R queryParam(String key, Object value, boolean encoded);

    default R queryParam(String key, Object value) {
        return queryParam(key, value, false);
    }

    R pathParam(String key, String value, boolean encoded);

    default R pathParam(String key, String value) {
        return pathParam(key, value, false);
    }

    R header(String key, String value);

    R headers(Map<String, String> headerMap);

    R appendUrl(String url);

    R useUrl(String url);

    R withObjectMapper(ObjectMapper objectMapper);

    String getUrl();

    HttpMethod getMethod();

    Map<String, String> getHeaders();

    default R contentType(String contentType) {
        return header("Content-Type", contentType);
    }

    default Optional<RequestBody> getBody() {
        return Optional.empty();
    }

    default boolean hasBody() {
        return getBody().isPresent();
    }
}
