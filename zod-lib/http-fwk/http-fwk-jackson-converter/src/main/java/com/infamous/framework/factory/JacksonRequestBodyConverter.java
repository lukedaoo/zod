package com.infamous.framework.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyAsByteArray;
import com.infamous.framework.http.core.BodyAsString;
import com.infamous.framework.http.core.BodyPart;

public class JacksonRequestBodyConverter<T> extends Converter<T, BodyPart> {

    public JacksonRequestBodyConverter(ObjectWriter objectWriter) {
        super(new ConvertProcessor<>(t -> {
            if (t instanceof String) {
                return new BodyAsString((String) t);
            } else if (t instanceof byte[]) {
                return new BodyAsByteArray((byte[]) t);
            } else {
                try {
                    return new BodyAsString(objectWriter.writeValueAsString(t));
                } catch (JsonProcessingException e) {
                    throw new ZodHttpException(e);
                }
            }
        }));
    }
}
