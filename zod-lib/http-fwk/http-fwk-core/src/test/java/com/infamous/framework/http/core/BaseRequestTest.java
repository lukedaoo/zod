package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpMethod;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BaseRequestTest {

    @Test
    public void test() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        BaseRequest baseRequest = new DummyBaseRequest("http://localhost:8080", "/upload/{fileId}/{sessionId}",
            HttpMethod.GET,
            objectMapper);

        HttpRequest request = new DummyBaseRequest(baseRequest);

        request.queryParam("group", Arrays.asList("g1", "g2"))
            .queryParam("filter", Arrays.asList("i dont know"), true)
            .queryParam("userId", "infamouSs")
            .queryParam("permission", "SomeThing Need Encoded", true)

            .pathParam("fileId", "file-id-1")
            .pathParam("sessionId", "session id", true)

            .header("OK-Header", "OK BRO")
            .headers(new HashMap<>() {{
                put("ClientId", "client-id-123");
            }})
            .contentType("application/json")
            .withObjectMapper(mock(ObjectMapper.class));

        assertFalse(request.getBody().isPresent());
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(
            "http://localhost:8080/upload/file-id-1/session+id"
                + "?group=g1&group=g2"
                + "&filter=i+dont+know"
                + "&userId=infamouSs"
                + "&permission=SomeThing+Need+Encoded",
            request.getUrl());
        Map<String, String> headers = request.getHeaders();
        assertEquals(3, headers.size());
        assertEquals("OK BRO", headers.get("OK-Header"));
        assertEquals("client-id-123", headers.get("ClientId"));
        assertEquals("application/json", headers.get("Content-Type"));

        request.appendUrl("/some-url");
        assertEquals(
            "http://localhost:8080/upload/file-id-1/session+id"
                + "?group=g1&group=g2"
                + "&filter=i+dont+know"
                + "&userId=infamouSs"
                + "&permission=SomeThing+Need+Encoded"
                + "/some-url",
            request.getUrl());

        request.useUrl("http://localhost:8080/i-had-a-dream-boy");
        assertEquals("http://localhost:8080/i-had-a-dream-boy", request.getUrl());
    }
}

class DummyBaseRequest extends BaseRequest {

    DummyBaseRequest(String baseUrl, String url, HttpMethod method,
                     ObjectMapper objectMapper) {
        super(baseUrl, url, method, objectMapper);
    }

    DummyBaseRequest(BaseRequest request) {
        super(request);
    }
}