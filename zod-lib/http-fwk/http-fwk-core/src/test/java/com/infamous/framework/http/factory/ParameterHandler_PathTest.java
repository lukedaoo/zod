package com.infamous.framework.http.factory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.PathParam;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.factory.ParameterHandler.Path;
import org.junit.jupiter.api.Test;

public class ParameterHandler_PathTest {


    @Test
    public void test() throws Exception {

        ParameterHandler.Path parameterHandler = new Path(getAnnotation(),
            new Converter(new ConvertProcessor(String::valueOf)));

        HttpRequest request = mock(HttpRequest.class);

        parameterHandler.apply(request, "1234");

        verify(request).pathParam("fileId", "1234", true);
        verifyNoMoreInteractions(request);
    }


    private PathParam getAnnotation() {
        return (PathParam) (RestClientTest.class.getMethods()[1]).getParameterAnnotations()[0][0];
    }
}


