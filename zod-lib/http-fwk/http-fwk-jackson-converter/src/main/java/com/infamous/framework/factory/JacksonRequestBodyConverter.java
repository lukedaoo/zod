package com.infamous.framework.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.BodyPartFactory;

public class JacksonRequestBodyConverter<T> extends Converter<T, BodyPart> {

    public JacksonRequestBodyConverter(ObjectWriter objectWriter) {
        super(new ConvertProcessor<>(t -> {
            BodyPart<?> bodyPart = BodyPartFactory.body(t);
            try {
                return bodyPart != null
                    ? bodyPart
                    : BodyPartFactory.body(objectWriter.writeValueAsString(t));
            } catch (JsonProcessingException e) {
                throw new ZodHttpException(e);
            }
        }));
    }
}
