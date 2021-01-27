package com.infamous.framework.http.engine;

import com.infamous.framework.http.core.HttpRequest;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Map;

public class JavaHttpEngine implements CallEngine {

    private static JavaHttpEngine INSTANCE = new JavaHttpEngine();

    private JavaHttpEngine() {
    }

    public static JavaHttpEngine getInstance() {
        return INSTANCE;
    }

    @Override
    public Call transform(Type returnType, HttpRequest request) {
        java.net.http.HttpRequest.Builder javaHttpRequestBuilder =
            java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(request.getUrl()));

        javaHttpRequestBuilder = JavaHttpMapper.mapBody(javaHttpRequestBuilder, request);

        Map<String, String> headers = request.getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            javaHttpRequestBuilder.header(entry.getKey(), entry.getValue());
        }

        return new JavaHttpCall(request, javaHttpRequestBuilder.build(), returnType);
    }
}
