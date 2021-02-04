package com.infamous.framework.http.engine;

import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RawHttpResponse;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

public class JavaHttpCall implements Call {

    private static final HttpClient CLIENT = HttpClient.newBuilder().build();
    private final HttpRequest m_rawRequest;
    private final Type m_returnType;
    private final java.net.http.HttpRequest m_request;

    public JavaHttpCall(HttpRequest rawRequest, java.net.http.HttpRequest request, Type returnType) {
        m_rawRequest = rawRequest;
        m_request = request;
        m_returnType = returnType;
    }

    @Override
    public RawHttpResponse execute() {
        HttpClient client = HttpClient.newBuilder().build();
        try {

            var response = client.send(m_request, getBodyHandler());
            return new JavaHttpResponse(response);
        } catch (Exception e) {
            throw new ZodHttpException(e);
        }
    }

    @Override
    public CompletableFuture<RawHttpResponse> executeAsync() {
        try {
            return CLIENT.sendAsync(m_request, getBodyHandler())
                .thenApply(o -> new JavaHttpResponse((HttpResponse) o));
        } catch (Exception e) {
            throw new ZodHttpException(e);
        }
    }

    @Override
    public HttpRequest rawRequest() {
        return m_rawRequest;
    }

    @Override
    public java.net.http.HttpRequest request() {
        return m_request;
    }

    private BodyHandler getBodyHandler() {
        if (m_returnType == byte[].class) {
            return BodyHandlers.ofByteArray();
        }
        if (m_returnType == InputStream.class || m_returnType == File.class) {
            return BodyHandlers.ofInputStream();
        }
        return BodyHandlers.ofString();
    }
}
