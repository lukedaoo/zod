package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
        verify(request, times(6)).part(any(BodyPart.class));
        verifyNoMoreInteractions(request);
    }

    @Test
    public void test_whenValueIsCollection() throws Exception {
        int[] times = {0};
        boolean[] invoked = new boolean[]{false, false};
        HttpRequestWithBody request = mockHttpRequest(HttpRequestWithBody.class);
        HttpRequestMultiPart requestMultiPart = mock(HttpRequestMultiPart.class);

        doAnswer((invocationOnMock -> {
            times[0]++;
            if (times[0] == 1) {
                BodyPart bodyPart = invocationOnMock.getArgument(0);
                assertEquals("files", bodyPart.getName());
                assertEquals("12", bodyPart.getValue());
                assertEquals("application/x-www-form-urlencoded", bodyPart.getContentType());

                invoked[0] = true;

                return requestMultiPart;
            }
            return null;
        })).when(request).part(any(BodyPart.class));

        doAnswer(invocationOnMock -> {
            times[0]++;
            if (times[0] == 2) {
                BodyPart bodyPart = invocationOnMock.getArgument(0);
                assertEquals("files", bodyPart.getName());
                assertEquals("123", bodyPart.getValue());
                assertEquals("application/x-www-form-urlencoded", bodyPart.getContentType());

                invoked[1] = true;
            }

            return null;
        }).when(requestMultiPart).part(any(BodyPart.class));

        ParameterHandler.Part parameterHandler = new ParameterHandler.Part(getAnnotation());

        apply(parameterHandler, request, Arrays.asList("12", "123"));

        assertEquals(times[0], 2);
        assertTrue(invoked[0]);
        assertTrue(invoked[1]);
    }

    @Test
    public void test_whenHttpRequestIsInstanceOfHttpRequestMultiPart() throws Exception {
        HttpRequestMultiPart request = testCaseFor(HttpRequestMultiPart.class);
        verify(request, times(6)).part(any(BodyPart.class));
        verifyNoMoreInteractions(request);
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
