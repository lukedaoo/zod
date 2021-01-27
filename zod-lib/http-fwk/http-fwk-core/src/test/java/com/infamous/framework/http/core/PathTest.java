package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.infamous.framework.http.ZodHttpException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class PathTest {


    @Test
    public void testPathWithBaseUrl() {
        assertDoesNotThrow(() -> new Path("http://localhost:8080", "/ok"));
    }

    @Test
    public void testPathWithoutBaseUrl() {
        assertDoesNotThrow(() -> new Path(null, "http://localhost:8080/ok"));
    }

    @Test
    public void testPathWithBaseUrl_ButWrongFormat_DoesNotStartWithProtocol() {
        Exception exp = assertThrows(ZodHttpException.class, () -> new Path("localhost:8080", "/ok"));
        assertEquals("java.lang.IllegalArgumentException: "
            + "Base url must start with http or https", exp.getMessage());
    }

    @Test
    public void testPathWithoutBaseUrl_AndRelativeUrl_IsNull() {
        Exception exp = assertThrows(ZodHttpException.class, () -> new Path(null, null));
        assertEquals("java.lang.IllegalArgumentException: url can not be NULL", exp.getMessage());
    }

    @Test
    public void testPathWithoutBaseUrl_AndRelativeUrl_IsNotNull_ButDoesNotStartWithProtocol() {
        Exception exp = assertThrows(ZodHttpException.class, () -> new Path(null, "localhost:8080"));
        assertEquals("java.lang.IllegalArgumentException: "
            + "url must start with http or https", exp.getMessage());
    }

    @Test
    public void testAppendPathParam() {
        Path path = newPath("/upload/{fileId}");
        path.appendPathParam("fileId", "1234", false);

        assertEquals("http://localhost:8080/upload/1234", path.getUrl());
    }

    @Test
    public void testAppendPathParamEncoded() {
        Path path = newPath("/upload/{fileId}");
        path.appendPathParam("fileId", "1234 5 7", true);

        assertEquals("http://localhost:8080/upload/1234+5+7", path.getUrl());
    }

    @Test
    public void testAppendPathParam_PatternNotFound() {
        Path path = newPath("/upload/{fileId");
        Exception exp = assertThrows(ZodHttpException.class, () -> path.appendPathParam("fileId", "1234 5 7", true));
        assertEquals("@PathParam must has format {path}. Invalid for path: fileId", exp.getMessage());
    }

    @Test
    public void testAppendQueryParam() {
        Path path = newPath("/upload");
        path.appendQueryParam("fileId", "1234 5 7", false);

        assertEquals("http://localhost:8080/upload?fileId=1234 5 7", path.getUrl());
    }

    @Test
    public void testAppendQueryParam_WithEncoded() {
        Path path = newPath("/upload");
        path.appendQueryParam("fileId", "1234 5 7", true);

        assertEquals("http://localhost:8080/upload?fileId=1234+5+7", path.getUrl());
    }

    @Test
    public void testAppendQueryParam_WithCollection() {
        Path path = newPath("/upload");
        path.appendQueryParam("group", Arrays.asList("g1", "g2", "g3"), false);

        assertEquals("http://localhost:8080/upload?group=g1&group=g2&group=g3", path.getUrl());
    }


    @Test
    public void testAppendUrl() {
        Path path = newPath("/upload");
        path.appendUrl("/test");

        assertEquals("http://localhost:8080/upload/test", path.getUrl());
    }

    @Test
    public void testUseUrl() {
        Path path = newPath("");
        path.useUrl("http://localhost/9090");

        assertEquals("http://localhost/9090", path.getUrl());
    }

    @Test
    public void testUseUrlWithoutProtocol() {
        Path path = newPath("");
        Exception exp = assertThrows(ZodHttpException.class, () -> path.useUrl("localhost/9090"));
        assertEquals("java.lang.IllegalArgumentException: url must start with http or https", exp.getMessage());
    }


    private Path newPath(String s) {
        return new Path("http://localhost:8080", s);
    }

}