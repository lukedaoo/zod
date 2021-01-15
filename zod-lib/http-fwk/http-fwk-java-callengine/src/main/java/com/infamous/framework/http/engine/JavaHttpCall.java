package com.infamous.framework.http.engine;

import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.HttpRequestBody;
import com.infamous.framework.http.core.HttpRequestBodyEntity;
import com.infamous.framework.http.core.HttpRequestMultiPart;
import com.infamous.framework.http.core.RawHttpResponse;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class JavaHttpCall implements Call {

    private HttpRequest m_rawRequest;
    private java.net.http.HttpRequest m_request;

    public JavaHttpCall(HttpRequest rawRequest, java.net.http.HttpRequest request) {
        m_rawRequest = rawRequest;
        m_request = request;
    }

    @Override
    public RawHttpResponse execute() {
        HttpClient client = HttpClient.newBuilder().build();
        try {

            var response = client.send(m_request, BodyHandlers.ofString(getCharset()));
            return new JavaHttpResponse(response);
        } catch (Exception e) {
            throw new ZodHttpException(e);
        }
    }

    @Override
    public CompletableFuture<RawHttpResponse> executeAsync() {
        HttpClient client = HttpClient.newBuilder().build();
        try {
            return client.sendAsync(m_request, BodyHandlers.ofString(getCharset()))
                .thenApply(JavaHttpResponse::new);
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

    private Charset getCharset() {
        if (m_rawRequest instanceof HttpRequestBody) {
            return ((HttpRequestBody) m_rawRequest).getCharset();
        }
        if (m_rawRequest instanceof HttpRequestMultiPart) {
            return ((HttpRequestMultiPart) m_rawRequest).getCharset();
        }
        if (m_rawRequest instanceof HttpRequestBodyEntity) {
            return ((HttpRequestBodyEntity) m_rawRequest).getCharset();
        }
        return StandardCharsets.UTF_8;
    }
}
