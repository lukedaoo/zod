package com.infamous.framework.http.core;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpMethod;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpRequestBody extends BaseRequest<HttpRequestWithBody> implements HttpRequestWithBody {

    private Charset m_charset = StandardCharsets.UTF_8;

    public HttpRequestBody(String baseUrl, String url, HttpMethod method, ObjectMapper objectMapper) {
        super(baseUrl, url, method, objectMapper);
    }

    @Override
    public MultipartBody part(String name, Object value, String contentType) {
        return new HttpRequestMultiPart(this).part(name, value, contentType);
    }

    @Override
    public MultipartBody part(String name, File file, String contentType) {
        return new HttpRequestMultiPart(this).part(name, file, contentType);
    }

    @Override
    public MultipartBody part(String name, InputStream stream, String contentType, String fileName) {
        return new HttpRequestMultiPart(this).part(name, stream, contentType, fileName);
    }

    @Override
    public MultipartBody part(String name, byte[] bytes, String contentType, String fileName) {
        return new HttpRequestMultiPart(this).part(name, bytes, contentType, fileName);
    }

    @Override
    public MultipartBody part(BodyPart multiPartInfo) {
        return new HttpRequestMultiPart(this).part(multiPartInfo);
    }

    @Override
    public HttpRequestWithBody charset(Charset charset) {
        m_charset = charset;
        return this;
    }

    @Override
    public RequestBodyEntity body(String body) {
        return new HttpRequestBodyEntity(this).body(body);
    }

    @Override
    public RequestBodyEntity body(Object body) {
        return new HttpRequestBodyEntity(this).body(body);
    }

    @Override
    public RequestBodyEntity body(byte[] body) {
        return new HttpRequestBodyEntity(this).body(body);
    }

    @Override
    public RequestBodyEntity body(InputStream is) {
       return new HttpRequestBodyEntity(this).body(is);
    }

    @Override
    public RequestBodyEntity body(File file) {
        return new HttpRequestBodyEntity(this).body(file);
    }

    @Override
    public RequestBodyEntity body(BodyPart<?> bodyPart) {
        return new HttpRequestBodyEntity(this).body(bodyPart);
    }

    @Override
    public Charset getCharset() {
        return m_charset;
    }
}
