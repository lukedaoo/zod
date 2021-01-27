package com.infamous.framework.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.RawHttpResponse;
import org.junit.jupiter.api.Test;

class JacksonConverterFactoryTest {


    @Test
    public void testCreateDefault() {
        JacksonConverterFactory converterFactory = JacksonConverterFactory.create();
        assertNotNull(converterFactory);
        assertNotNull(converterFactory.objectMapper());
    }

    @Test
    public void testCreateWithObjectMapper() {
        JacksonConverterFactory converterFactory = JacksonConverterFactory.create(mock(ObjectMapper.class));
        assertNotNull(converterFactory);
        assertNotNull(converterFactory.objectMapper());
    }

    @Test
    public void testGetRequestConverter() {
        JacksonConverterFactory converterFactory = JacksonConverterFactory.create();

        Converter<String, BodyPart> c1 = converterFactory.requestBodyConverter(String.class);
        BodyPart b1 = c1.converter("ok");
        assertEquals("ok", b1.getValue());

        Converter<RequestEntity, BodyPart> c2 = converterFactory.requestBodyConverter(RequestEntity.class);
        BodyPart b2 = c2.converter(new RequestEntity("Luke D"));
        assertEquals("{\"name\":\"Luke D\"}", b2.getValue());
    }

    @Test
    public void testGetResponseConverter() {
        JacksonConverterFactory converterFactory = JacksonConverterFactory.create();

        Converter<RawHttpResponse, RequestEntity> converter = converterFactory
            .responseBodyConverter(RequestEntity.class);
        RequestEntity requestEntity = converter.converter(mockHttpResponse("{\"name\":\"Luke D\"}"));
        assertEquals("Luke D", requestEntity.getName());
    }

    @Test
    public void testReadWriteWithObjectMapper() {
        JacksonConverterFactory converterFactory = JacksonConverterFactory.create();
        com.infamous.framework.converter.ObjectMapper objectMapper = converterFactory.objectMapper();

        RequestEntity requestEntity = objectMapper.readValue("{\"name\":\"Luke D\"}", RequestEntity.class);
        assertNotNull(requestEntity);
        assertEquals("Luke D", requestEntity.getName());

        String value = objectMapper.writeValue(new RequestEntity("infamouSs"));
        assertNotNull(value);
        assertEquals("{\"name\":\"infamouSs\"}", value);
    }

    private RawHttpResponse mockHttpResponse(String value) {
        RawHttpResponse rawHttpResponse = mock(RawHttpResponse.class);
        when(rawHttpResponse.getContentAsString()).thenReturn(value);
        return rawHttpResponse;
    }
}

class RequestEntity {

    String name;

    public RequestEntity() {

    }

    public RequestEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}