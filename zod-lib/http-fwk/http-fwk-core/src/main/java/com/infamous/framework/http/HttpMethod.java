package com.infamous.framework.http;

import java.util.Arrays;

public enum HttpMethod {
    GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD, TRACE;


    public static HttpMethod from(String method) {
        return Arrays.stream(HttpMethod.values())
            .filter(httpMethodEnum -> httpMethodEnum.name().equalsIgnoreCase(method))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No support method " + method));
    }
}
