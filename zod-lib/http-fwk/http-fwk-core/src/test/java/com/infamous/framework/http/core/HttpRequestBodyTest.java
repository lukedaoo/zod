package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpMethod;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class HttpRequestBodyTest {


    @Test
    public void test_WithMultipart() {

        HttpRequestBody requestBody = new HttpRequestBody("http://localhost:8080", "/upload", HttpMethod.GET,
            mock(ObjectMapper.class));

        requestBody.noCharset();
        assertNull(requestBody.getCharset());

        HttpRequestMultiPart multiPart1 = (HttpRequestMultiPart) requestBody
            .part("file", mock(File.class), "application/file");
        assertNotNull(multiPart1);

        HttpRequestMultiPart multiPart2 = (HttpRequestMultiPart) requestBody
            .part("name", "ErenJeager", "text/plain");
        assertNotNull(multiPart2);

        HttpRequestMultiPart multiPart3 = (HttpRequestMultiPart) requestBody
            .part("inputStream", mock(InputStream.class), "application/input-stream", "aot.txt");
        assertNotNull(multiPart3);

        HttpRequestMultiPart multiPart4 = (HttpRequestMultiPart) requestBody
            .part("byteArr", new byte[0], "application/byte-array", "lalavidas.txt");
        assertNotNull(multiPart4);

        HttpRequestMultiPart multiPart5 = (HttpRequestMultiPart) requestBody
            .part(new BodyPart("inputStreamWithFileName", mock(InputStream.class), null) {
                @Override
                public boolean isFile() {
                    return true;
                }

                @Override
                public String getFileName() {
                    return "ok.txt";
                }
            });
        assertNotNull(multiPart5);
    }

    @Test
    public void test_WithBody() {

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.writeValue(any())).thenReturn("demo-converted");

        HttpRequestBody requestBody = new HttpRequestBody("http://localhost:8080", "/upload", HttpMethod.GET,
            objectMapper);

        requestBody.charset(StandardCharsets.UTF_16);
        assertEquals(StandardCharsets.UTF_16, requestBody.getCharset());

        HttpRequestBodyEntity body1 = (HttpRequestBodyEntity) requestBody
            .body(new byte[0]);
        assertNotNull(body1.entity());
        assertTrue(body1.entity().getValue() instanceof byte[]);

        HttpRequestBodyEntity body2 = (HttpRequestBodyEntity) requestBody
            .body("String");
        assertNotNull(body2.entity());
        assertTrue(body2.entity().getValue() instanceof String);

        HttpRequestBodyEntity body3 = (HttpRequestBodyEntity) requestBody
            .body(new BodyPart(null, Boolean.FALSE, null) {
                @Override
                public boolean isFile() {
                    return false;
                }
            });
        assertNotNull(body3.entity());
        assertTrue(body3.entity().getValue() instanceof Boolean);

        // Convert DemoObject to String
        HttpRequestBodyEntity body4 = (HttpRequestBodyEntity) requestBody.body(new DemoObject());
        assertNotNull(body4.entity());
        assertTrue(body4.entity().getValue() instanceof String);

        HttpRequestBodyEntity body5 = (HttpRequestBodyEntity) requestBody.body(mock(InputStream.class));
        assertNotNull(body5.entity());
        assertTrue(body5.entity().getValue() instanceof InputStream);

        HttpRequestBodyEntity body6 = (HttpRequestBodyEntity) requestBody.body(mock(File.class));
        assertNotNull(body6.entity());
        assertTrue(body6.entity().getValue() instanceof File);
    }
}