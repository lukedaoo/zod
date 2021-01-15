package com.infamous.framework.http.core;

import java.nio.charset.Charset;

public interface RequestBodyEntity extends HttpRequest<RequestBodyEntity>, RequestBody {
    RequestBodyEntity body(byte[] bodyBytes);

    RequestBodyEntity body(String bodyAsString);

    RequestBodyEntity body(Object body);

    RequestBodyEntity body(BodyPart<?> bodyPart);

    RequestBodyEntity charset(Charset charset);

    default RequestBodyEntity noCharset() {
        return charset(null);
    }
}
