package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.Rest;
import com.infamous.framework.http.RestClient;
import com.infamous.framework.http.engine.Call;
import com.infamous.framework.http.engine.CallEngine;
import com.infamous.framework.logging.core.AdvancedLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class ServiceProxyTest {

    private ZodHttpClientFactory m_factory;

    @BeforeEach
    public void setup() {
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

        m_factory = ZodHttpClientFactory.builder()
            .baseUrl("http://localhost:8080")
            .config(new HttpConfig())
            .callEngine(callEngine)
            .converterFactory(cf)
            .logger(mock(AdvancedLogger.class))
            .build();
    }

    @Test
    public void testCreateAndInvoke() {
        RestClientService service = ServiceProxy.create(m_factory, RestClientService.class);

        assertNotEquals(0, service.hashCode());
        assertEquals(1, service.defaultMethod());
        assertEquals(99, service.test());
    }
}

@RestClient(category = "restClientTest")
interface RestClientService {

    @Rest(method = HttpMethod.GET, url = "/test")
    int test();

    default int defaultMethod() {
        return 1;
    }
}