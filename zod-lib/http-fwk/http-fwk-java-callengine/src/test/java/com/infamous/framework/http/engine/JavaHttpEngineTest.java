package com.infamous.framework.http.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.core.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class JavaHttpEngineTest {


    @Test
    public void testTransform() {
        HttpRequest request = mock(com.infamous.framework.http.core.HttpRequest.class);

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");

        when(request.getUrl()).thenReturn("http://localhost:8800");
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getBody()).thenReturn(Optional.empty());
        when(request.getHeaders()).thenReturn(map);

        Call call = JavaHttpEngine.getInstance().transform(String.class, request);

        assertNotNull(call);
        assertEquals(request, call.rawRequest());
        assertTrue(call.request() instanceof java.net.http.HttpRequest);
    }
}