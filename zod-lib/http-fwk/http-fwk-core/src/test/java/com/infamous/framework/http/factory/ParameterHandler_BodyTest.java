package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyAsString;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.HttpRequestWithBody;
import com.infamous.framework.http.factory.ParameterHandler.Body;
import org.junit.jupiter.api.Test;

public class ParameterHandler_BodyTest {

    @Test
    public void test_WrongTypeHttpRequest() throws Exception {

        ParameterHandler.Body parameterHandler = new Body(mock(Converter.class));

        HttpRequest request = mock(HttpRequest.class);

        Exception exp = assertThrows(ZodHttpException.class, () -> parameterHandler.apply(request, "1234"));
        assertTrue(exp.getMessage().contains("HttpRequest type doesn't support @Body param. Type:"));
    }

    @Test
    public void test() throws Exception {

        Converter converter = mock(Converter.class);
        when(converter.converter(any())).then(invocationOnMock -> new BodyAsString("123"));

        ParameterHandler.Body parameterHandler = new Body(converter);

        HttpRequestWithBody request = mock(HttpRequestWithBody.class);

        parameterHandler.apply(request, "1234");
        parameterHandler.apply(request, new byte[]{1, 2});
        parameterHandler.apply(request, mock(BodyPart.class));
        parameterHandler.apply(request, new Object());

        verify(request).body(any(String.class));
        verify(request).body(any(byte[].class));
        verify(request, times(2)).body(any(BodyPart.class));

        verifyNoMoreInteractions(request);
    }

}
