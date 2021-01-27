package com.infamous.framework.http.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JavaHttpResponseTest {


    @Test
    public void testJavaHttpResponse() throws IOException {
        String body = "json-string";
        HttpResponse response = mockResponse(200, body);
        JavaHttpResponse javaHttpResponse = new JavaHttpResponse(response);

        assertTrue(javaHttpResponse.isSuccess());
        assertEquals(body.getBytes().length, javaHttpResponse.getContentAsBytes().length);
        assertEquals("application/json", javaHttpResponse.getContentType());
        assertEquals(body, javaHttpResponse.getContentAsString());
        assertEquals(body.getBytes().length, javaHttpResponse.getContent().readAllBytes().length);

        assertEquals(body, javaHttpResponse.getStatusText());
        assertEquals(1, javaHttpResponse.getHeaders().size());
    }

    @Test
    public void testJavaHttpResponseWithByteArray() throws IOException {
        byte[] body = new byte[2];
        HttpResponse response = mockResponse(200, body);
        JavaHttpResponse javaHttpResponse = new JavaHttpResponse(response);

        assertTrue(javaHttpResponse.isSuccess());
        assertEquals(body.length, javaHttpResponse.getContentAsBytes().length);
        assertEquals("application/json", javaHttpResponse.getContentType());
        assertNotNull(javaHttpResponse.getContentAsString());
        assertEquals(body.length, javaHttpResponse.getContent().readAllBytes().length);

        assertNotNull(javaHttpResponse.getStatusText());
        assertEquals(1, javaHttpResponse.getHeaders().size());
    }

    private HttpResponse mockResponse(int statusCode, Object body) {
        HttpResponse response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(statusCode);
        when(response.body()).thenReturn(body);

        Map<String, List<String>> headMaps = new HashMap<>();
        headMaps.put("Content-Type", Arrays.asList("application/json", "application/xml"));

        HttpHeaders headers = HttpHeaders.of(headMaps, (s, s2) -> true);
        when(response.headers()).thenReturn(headers);
        return response;
    }
}