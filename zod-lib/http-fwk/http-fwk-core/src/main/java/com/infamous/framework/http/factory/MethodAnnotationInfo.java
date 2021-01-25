package com.infamous.framework.http.factory;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.Headers;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.Rest;
import com.infamous.framework.http.core.HttpRequestWithBody;
import com.infamous.framework.http.core.HttpRestClientCreation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class MethodAnnotationInfo {

    Headers m_headers;
    Rest m_rest;

    Map<String, String> extractHeaders(Method method) {
        if (m_headers == null) {
            return Collections.emptyMap();
        }

        String[] headerValues = m_headers.value();

        if (headerValues.length == 0) {
            throw Utils.methodError(method, "@Headers annotation is empty");
        }
        Map<String, String> headerMap = new HashMap<>(headerValues.length);

        for (String header : headerValues) {
            int colon = header.indexOf(':');
            if (colon == -1 || colon == 0 || colon == header.length() - 1) {
                throw Utils.methodError(method,
                    "@Headers value must be in the form \"Name: Value\". Found: " + header);
            }
            String headerName = header.substring(0, colon);
            String headerValue = header.substring(colon + 1).trim();

            headerMap.put(headerName, headerValue);
        }

        return headerMap;
    }

    HttpRequestWithBody extractToRequest(Method method, String baseUrl, HttpConfig config, ObjectMapper objectMapper) {
        if (m_rest == null) {
            throw Utils.methodError(method, "@Rest is required");
        }
        return HttpRestClientCreation.getInstance(config).create(baseUrl, m_rest.url(), m_rest.method(), objectMapper);
    }
}
