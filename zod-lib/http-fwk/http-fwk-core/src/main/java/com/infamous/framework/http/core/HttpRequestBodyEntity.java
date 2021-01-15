package com.infamous.framework.http.core;

import java.nio.charset.Charset;
import java.util.Optional;

public class HttpRequestBodyEntity extends BaseRequest<RequestBodyEntity> implements RequestBodyEntity {

    private Charset m_charset;
    private BodyPart m_body;

    HttpRequestBodyEntity(HttpRequestBody request) {
        super(request);
        m_charset = request.getCharset();
    }


    @Override
    public RequestBodyEntity body(byte[] bodyBytes) {
        m_body = new BodyAsByteArray(bodyBytes);
        return this;
    }

    @Override
    public RequestBodyEntity body(String bodyAsString) {
        m_body = new BodyAsString(bodyAsString);
        return this;
    }

    @Override
    public RequestBodyEntity body(Object body) {
        return body(getObjectMapper().writeValue(body));
    }

    @Override
    public RequestBodyEntity body(BodyPart<?> bodyPart) {
        m_body = bodyPart;
        return this;
    }

    @Override
    public RequestBodyEntity charset(Charset charset) {
        m_charset = charset;
        return this;
    }

    @Override
    public boolean isBodyEntity() {
        return true;
    }

    @Override
    public boolean isMultiPart() {
        return false;
    }

    @Override
    public Optional<RequestBody> getBody() {
        return Optional.of(this);
    }

    @Override
    public Charset getCharset() {
        return m_charset;
    }

    @Override
    public BodyPart entity() {
        return m_body;
    }
}
