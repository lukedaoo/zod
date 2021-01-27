package com.infamous.framework.http.core;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface RequestBodyEntity extends HttpRequest<RequestBodyEntity>, RequestBody {
    RequestBodyEntity body(byte[] bodyBytes);

    RequestBodyEntity body(String bodyAsString);

    RequestBodyEntity body(InputStream is);

    RequestBodyEntity body(File file);

    RequestBodyEntity body(Object body);

    RequestBodyEntity body(BodyPart<?> bodyPart);

    RequestBodyEntity charset(Charset charset);

    default RequestBodyEntity noCharset() {
        return charset(null);
    }
}
