package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.infamous.framework.http.Part;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.HttpRequestMultiPart;
import com.infamous.framework.http.core.HttpRequestWithBody;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class ParameterHandler_PartTest {


    @Test
    public void test_whenHttpDoesNotSupportPart() throws Exception {

        ParameterHandler.Part parameterHandler = new ParameterHandler.Part(getAnnotation());

        HttpRequest request = mock(HttpRequest.class);

        assertThrows(ZodHttpException.class, () -> parameterHandler.apply(request, "1234"));
    }

    @Test
    public void test_whenHttpRequestIsInstanceOfHttpRequestWithBody() throws Exception {
        HttpRequestWithBody request = testCaseFor(HttpRequestWithBody.class);
        verify(request).part("files", "1234", "");
        verify(request).part("files", 123, "");
        verify(request).part(eq("files"), any(byte[].class), eq(""), eq(""));
        verify(request).part(eq("files"), any(InputStream.class), eq(""), eq(""));
        verify(request).part(eq("files"), any(File.class), eq(""));
        verify(request).part(any(BodyPart.class));
        verifyNoMoreInteractions(request);
    }

    @Test
    public void test_whenValueIsCollection() throws Exception {
        int[] times = {0};
        HttpRequestWithBody request = mockHttpRequest(HttpRequestWithBody.class);
        HttpRequestMultiPart requestMultiPart = mock(HttpRequestMultiPart.class);

        doAnswer((invocationOnMock -> {
            times[0]++;
            if (times[0] == 1) {
                assertEquals("files", invocationOnMock.getArgument(0));
                assertEquals("12", invocationOnMock.getArgument(1));
                assertEquals("", invocationOnMock.getArgument(2));
                return requestMultiPart;
            }
            return null;
        })).when(request).part(anyString(), anyString(), anyString());

        ParameterHandler.Part parameterHandler = new ParameterHandler.Part(getAnnotation());

        apply(parameterHandler, request, Arrays.asList("12", "123"));

        assertEquals(times[0], 1);
    }

    @Test
    public void test_whenHttpRequestIsInstanceOfHttpRequestMultiPart() throws Exception {
        HttpRequestMultiPart request = testCaseFor(HttpRequestMultiPart.class);
    }

    private <T> T mockHttpRequest(Class<T> clazz) {
        return mock(clazz);
    }

    private HttpRequest apply(ParameterHandler.Part partHandler, HttpRequest request, Object data) throws Exception {
        return partHandler.apply(request, data);
    }

    private <T> T testCaseFor(Class<T> clazz) throws Exception {
        ParameterHandler.Part parameterHandler = new ParameterHandler.Part(getAnnotation());

        T request = mockHttpRequest(clazz);
        apply(parameterHandler, (HttpRequest) request, "1234");
        apply(parameterHandler, (HttpRequest) request, 123);
        byte[] bytes = new byte[0];
        apply(parameterHandler, (HttpRequest) request, bytes);
        apply(parameterHandler, (HttpRequest) request, mock(InputStream.class));
        apply(parameterHandler, (HttpRequest) request, mock(File.class));
        apply(parameterHandler, (HttpRequest) request, mock(BodyPart.class));

        return request;
    }


    private Part getAnnotation() throws NoSuchMethodException {
        return (Part) (RestClientTest.class.getMethod("testWithMultipartBody", String.class))
            .getParameterAnnotations()[0][0];
    }
}
