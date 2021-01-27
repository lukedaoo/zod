package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.ObjectMapper;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpRequestBodyEntityTest {

    private HttpRequestBody m_httpRequestBody;

    @BeforeEach
    public void setup() {
        m_httpRequestBody = mock(HttpRequestBody.class);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.writeValue(any())).thenReturn("demo-converted");

        when(m_httpRequestBody.getObjectMapper()).thenReturn(objectMapper);
    }

    @Test
    public void test() {
        HttpRequestBodyEntity requestBodyEntity = new HttpRequestBodyEntity(m_httpRequestBody);

        String jsonString = """
                {"jsonString": "okbro"} 
            """;

        requestBodyEntity = (HttpRequestBodyEntity) requestBodyEntity.charset(StandardCharsets.UTF_16);
        assertEquals(StandardCharsets.UTF_16, requestBodyEntity.getCharset());

        requestBodyEntity = (HttpRequestBodyEntity) requestBodyEntity.body(new byte[0]);
        assertTrue(requestBodyEntity.hasBody());
        assertTrue(requestBodyEntity.getBody().get().entity() instanceof BodyAsByteArray);

        requestBodyEntity = (HttpRequestBodyEntity) requestBodyEntity.body(jsonString);
        assertTrue(requestBodyEntity.hasBody());
        assertTrue(requestBodyEntity.getBody().get().entity() instanceof BodyAsString);
        assertEquals(jsonString, requestBodyEntity.getBody().get().entity().getValue());

        requestBodyEntity = (HttpRequestBodyEntity) requestBodyEntity.body(new BodyPart(null, Boolean.FALSE, null) {
            @Override
            public boolean isFile() {
                return false;
            }
        });
        assertTrue(requestBodyEntity.hasBody());
        assertTrue(requestBodyEntity.getBody().get().entity() instanceof BodyPart);

        requestBodyEntity = (HttpRequestBodyEntity) requestBodyEntity.body(new DemoObject());
        assertTrue(requestBodyEntity.hasBody());
        assertTrue(requestBodyEntity.getBody().get().entity() instanceof BodyAsString);
        assertEquals("demo-converted", requestBodyEntity.getBody().get().entity().getValue());

        requestBodyEntity = (HttpRequestBodyEntity) requestBodyEntity.body(mock(InputStream.class));
        assertTrue(requestBodyEntity.hasBody());
        assertTrue(requestBodyEntity.getBody().get().entity() instanceof BodyAsInputStream);

        requestBodyEntity = (HttpRequestBodyEntity) requestBodyEntity.body(mock(File.class));
        assertTrue(requestBodyEntity.hasBody());
        assertTrue(requestBodyEntity.getBody().get().entity() instanceof BodyAsFile);

        assertFalse(requestBodyEntity.isMultiPart());
        assertTrue(requestBodyEntity.isBodyEntity());
        assertFalse(requestBodyEntity.isEmpty());
    }
}

