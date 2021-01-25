package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.Header;
import com.infamous.framework.http.core.HttpRequest;
import org.junit.jupiter.api.Test;

public class ParameterHandler_HeaderTest {

    @Test
    public void test() throws Exception {
        com.infamous.framework.http.Header headerAnnotation = getAnnotation();
        assertNotNull(headerAnnotation);
        ParameterHandler.Header parameterHandler = new ParameterHandler.Header(headerAnnotation,
            new Converter<>(new ConvertProcessor<>(String::valueOf)));

        HttpRequest request = mock(HttpRequest.class);

        parameterHandler.apply(request, 1);

        verify(request).header("Dynamic-Header", "1");
        verifyNoMoreInteractions(request);
    }

    private Header getAnnotation() throws NoSuchMethodException {
        return (Header) (RestClientTest.class.getMethod("testHeader", String.class)).getParameterAnnotations()[0][0];
    }
}

