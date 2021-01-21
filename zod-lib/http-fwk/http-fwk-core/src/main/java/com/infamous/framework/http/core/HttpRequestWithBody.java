package com.infamous.framework.http.core;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface HttpRequestWithBody extends HttpRequest<HttpRequestWithBody> {

    MultipartBody multiPartContent();

    MultipartBody part(String name, Object value, String contentType);

    MultipartBody part(String name, File file, String contentType);

    MultipartBody part(String name, InputStream stream, String contentType, String fileName);

    MultipartBody part(String name, byte[] bytes, String contentType, String fileName);

    MultipartBody part(BodyPart multiPartInfo);

    HttpRequestWithBody charset(Charset charset);

    default HttpRequestWithBody noCharset() {
        return charset(null);
    }

    RequestBodyEntity body(String body);

    RequestBodyEntity body(Object body);

    RequestBodyEntity body(byte[] body);

    RequestBodyEntity body(BodyPart<?> bodyPart);

    Charset getCharset();
}
