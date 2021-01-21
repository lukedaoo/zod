package com.infamous.framework.http.core;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class HttpRequestMultiPart extends BaseRequest<MultipartBody> implements MultipartBody {

    private Set<BodyPart> m_parameters = new LinkedHashSet<>();
    private Charset m_charset;

    public HttpRequestMultiPart(HttpRequestBody httpRequest) {
        super(httpRequest);
        m_charset = httpRequest.getCharset();
    }


    @Override
    public MultipartBody part(String name, String value, String contentType) {
        addPart(name, value, contentType);
        return this;
    }

    @Override
    public MultipartBody part(String name, File file, String contentType) {
        addPart(name, file, contentType);
        return this;
    }

    @Override
    public MultipartBody part(String name, InputStream stream, String contentType, String fileName) {
        addPart(new InputStreamPart(name, stream, contentType, fileName));
        return this;
    }

    @Override
    public MultipartBody part(String name, byte[] bytes, String contentType, String fileName) {
        addPart(new ByteArrayPart(name, bytes, contentType, fileName));
        return null;
    }

    @Override
    public MultipartBody part(BodyPart multiPartInfo) {
        addPart(multiPartInfo);
        return this;
    }

    @Override
    public MultipartBody charset(Charset charset) {
        m_charset = charset;
        return this;
    }

    public MultipartBody part(String name, Object value, String contentType) {
        addPart(name, value, contentType);
        return this;
    }

    private void addPart(String name, Object value, String contentType) {
        if (value instanceof InputStream) {
            addPart(new InputStreamPart(name, (InputStream) value, contentType));
        } else if (value instanceof File) {
            addPart(new FilePart((File) value, name, contentType));
        } else {
            addPart(new ParamPart(name, value, contentType));
        }
    }

    private void addPart(BodyPart<?> value) {
        m_parameters.add(value);
    }

    @Override
    public boolean isBodyEntity() {
        return false;
    }

    @Override
    public boolean isMultiPart() {
        return true;
    }

    @Override
    public Charset getCharset() {
        return m_charset;
    }

    @Override
    public Optional<RequestBody> getBody() {
        return Optional.of(this);
    }

    @Override
    public Collection<BodyPart> multiParts() {
        return m_parameters;
    }
}
