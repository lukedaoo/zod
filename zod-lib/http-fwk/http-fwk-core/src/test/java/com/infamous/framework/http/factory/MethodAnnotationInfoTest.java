package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.Headers;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.Rest;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.HttpRequest;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MethodAnnotationInfoTest {

    @Test
    public void testExtractHeader() throws NoSuchMethodException {
        MethodAnnotationInfo methodAnnotationInfo = new MethodAnnotationInfo();
        methodAnnotationInfo.m_headers = getHeaders("testStaticHeader");

        Map<String, String> headers = methodAnnotationInfo.extractHeaders(getMethod("testStaticHeader"));

        assertFalse(headers.isEmpty());
        assertEquals(2, headers.size());
        assertEquals("application/xml", headers.get("Content-Type"));
        assertEquals("Static-Header-Value", headers.get("Static-Header-Name"));
    }

    @Test
    public void testExtractHeader_WhenValuesIsEmpty() throws Exception {
        MethodAnnotationInfo methodAnnotationInfo = new MethodAnnotationInfo();
        methodAnnotationInfo.m_headers = getHeaders("testEmptyHeader");

        Exception exception = assertThrows(ZodHttpException.class,
            () -> methodAnnotationInfo.extractHeaders(getMethod("testEmptyHeader")));
        assertEquals("@Headers annotation is empty for method RestClientTest.testEmptyHeader", exception.getMessage());
    }


    @Test
    public void testExtractHeader_WhenValuesIsInvalid() throws Exception {
        MethodAnnotationInfo methodAnnotationInfo = new MethodAnnotationInfo();
        methodAnnotationInfo.m_headers = getHeaders("testHeadersInvalid");

        Exception exception = assertThrows(ZodHttpException.class,
            () -> methodAnnotationInfo.extractHeaders(getMethod("testHeadersInvalid")));
        assertEquals(
            "@Headers value must be in the form \"Name: Value\". Found: ContentType/application/xml for method RestClientTest.testHeadersInvalid",
            exception.getMessage());
    }

    @Test
    public void testExtractRequest() throws NoSuchMethodException {
        MethodAnnotationInfo methodAnnotationInfo = new MethodAnnotationInfo();
        methodAnnotationInfo.m_rest = getRest("testRest");

        HttpRequest request = methodAnnotationInfo
            .extractToRequest(getMethod("testRest"), "http://localhost:8080", new HttpConfig(), mock(
                ObjectMapper.class));

        assertNotNull(request);
    }

    @Test
    public void testExtractRequest_ButNotHaveRest() throws NoSuchMethodException {
        MethodAnnotationInfo methodAnnotationInfo = new MethodAnnotationInfo();
        methodAnnotationInfo.m_rest = null;

        assertThrows(ZodHttpException.class,
            () -> methodAnnotationInfo
                .extractToRequest(getMethod("testRest"), "http://localhost:8080", new HttpConfig(), mock(
                    ObjectMapper.class)));
    }

    private Method getMethod(String name) throws NoSuchMethodException {
        return RestClientTest.class.getMethod(name);
    }

    private Headers getHeaders(String name) throws NoSuchMethodException {
        return (Headers) getMethod(name).getAnnotations()[0];
    }

    private Rest getRest(String name) throws NoSuchMethodException {
        return (Rest) getMethod(name).getAnnotations()[0];
    }
}