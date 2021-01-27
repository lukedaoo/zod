package com.infamous.framework.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.RawHttpResponse;

public class JacksonResponseBodyConverter<T> extends Converter<RawHttpResponse, T> {

    public JacksonResponseBodyConverter(ObjectReader objectReader) {
        super(new ConvertProcessor<>(rawHttpResponse -> {
            try {
                return (T) objectReader.readValue(rawHttpResponse.getContentAsString());
            } catch (JsonProcessingException e) {
                throw new ZodHttpException(e);
            }
        }));
    }
}
