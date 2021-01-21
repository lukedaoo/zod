package com.infamous.framework.http.factory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.QueryParam;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.factory.ParameterHandler.Query;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.Test;

public class ParameterHandler_QueryTest {

    @Test
    public void test() throws Exception {

        ParameterHandler.Query parameterHandler = new Query(getAnnotation(),
            new Converter(new ConvertProcessor(String::valueOf)));

        HttpRequest request = mock(HttpRequest.class);

        parameterHandler.apply(request, "1234");

        verify(request).queryParam("id", "1234", true);
        verifyNoMoreInteractions(request);
    }

    @Test
    public void testApplyWithCollection() throws Exception {

        ParameterHandler.Query parameterHandler = new Query(getAnnotation(),
            new Converter(new ConvertProcessor(String::valueOf)));

        HttpRequest request = mock(HttpRequest.class);

        parameterHandler.apply(request, Arrays.asList("1", "2", "3"));

        verify(request).queryParam(eq("id"), any(Collection.class), eq(true));
        verifyNoMoreInteractions(request);
    }


    private QueryParam getAnnotation() {
        return (QueryParam) (RestClientTest.class.getMethods()[2]).getParameterAnnotations()[0][0];
    }
}
