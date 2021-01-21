package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.HttpRestClientCreation;
import com.infamous.framework.http.engine.Call;
import com.infamous.framework.http.engine.CallEngine;
import com.infamous.framework.logging.core.AdvancedLogger;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class RequestFactoryTest {

    @Test
    public void testCreateEngine() throws Exception {

        ConverterFactory cf = mock(ConverterFactory.class);
        when(cf.getObjectMapper()).thenReturn(mock(ObjectMapper.class));
        Converter converter = mock(Converter.class);
        when(converter.converter(any())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return 99;
            }
        });
        when(cf.responseBodyConverter(any())).thenReturn(converter);

        CallEngine callEngine = mock(CallEngine.class);
        when(callEngine.transformFrom(any(), any())).thenReturn(mock(Call.class));

        var factory = ZodHttpClientFactory.builder()
            .baseUrl("http://localhost:8080")
            .config(new HttpConfig())
            .callEngine(callEngine)
            .converterFactory(cf)
            .logger(mock(AdvancedLogger.class))
            .build();

        HttpRequest request = HttpRestClientCreation.getInstance(factory.config())
            .create("http://localhost:8080", "/test", HttpMethod.GET, factory.getObjectMapper());

        List<ParameterHandler<?>> parameterHandlers = Collections.singletonList(mockParameterHandler(request));
        RequestFactory requestFactory = new RequestFactory(factory, request, parameterHandlers);

        Call call = requestFactory.createCall(int.class, new Object[]{1});
        assertNotNull(call);
        verify(parameterHandlers.get(0)).apply(any(), any());
    }

    private ParameterHandler<?> mockParameterHandler(HttpRequest request) throws Exception {
        ParameterHandler parameterHandler = mock(ParameterHandler.class);

        when(parameterHandler.apply(any(), any())).thenReturn(request);

        return parameterHandler;
    }

}