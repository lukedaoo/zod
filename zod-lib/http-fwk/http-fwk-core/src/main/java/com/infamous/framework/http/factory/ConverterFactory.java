package com.infamous.framework.http.factory;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.RawHttpResponse;
import java.lang.reflect.Type;

public abstract class ConverterFactory {

    public ObjectMapper getObjectMapper() {
        return null;
    }

    public <T> Converter<T, BodyPart> requestBodyConverter(Type type) {
        return null;
    }

    public <T> Converter<RawHttpResponse, T> responseBodyConverter(Type type) {
        return null;
    }
}
