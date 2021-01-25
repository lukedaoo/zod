package com.infamous.framework.http.factory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.factory.ParameterHandler.Url;
import org.junit.jupiter.api.Test;

public class ParameterHandler_UrlTest {

    @Test
    public void test() throws Exception {

        ParameterHandler.Url parameterHandler = new Url(getAnnotation(false),
            new Converter(new ConvertProcessor(String::valueOf)));

        HttpRequest request = mock(HttpRequest.class);

        parameterHandler.apply(request, "1234");

        verify(request).appendUrl("1234");
        verifyNoMoreInteractions(request);
    }

    @Test
    public void testApplyWithFullUrl() throws Exception {
        ParameterHandler.Url parameterHandler = new Url(getAnnotation(true),
            new Converter(new ConvertProcessor(String::valueOf)));

        HttpRequest request = mock(HttpRequest.class);

        parameterHandler.apply(request, "useFullUrl");

        verify(request).useUrl("useFullUrl");
        verifyNoMoreInteractions(request);
    }


    private com.infamous.framework.http.Url getAnnotation(boolean withFullUrl) throws NoSuchMethodException {
        String methodName = !withFullUrl ? "testUrl" : "testUrlWithFullUrl";
        return (com.infamous.framework.http.Url) (RestClientTest.class.getMethod(methodName, String.class))
            .getParameterAnnotations()[0][0];
    }
}
