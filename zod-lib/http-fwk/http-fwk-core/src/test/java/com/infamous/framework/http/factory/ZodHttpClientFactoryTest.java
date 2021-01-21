package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.RestClient;
import com.infamous.framework.http.engine.CallEngine;
import com.infamous.framework.logging.core.AdvancedLogger;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZodHttpClientFactoryTest {


    private ConverterFactory m_cf;

    @BeforeEach
    public void setup() {
        m_cf = mock(ConverterFactory.class);
        when(m_cf.getObjectMapper()).thenReturn(mock(ObjectMapper.class));
    }


    @Test
    public void testBuilder_RequireConverterFactory() {
        assertThrows(NullPointerException.class, () -> ZodHttpClientFactory.builder()
            .baseUrl("base-url")
            .config(new HttpConfig())
            .callEngine(mock(CallEngine.class))
            .converterFactory(null)
            .build());
    }

    @Test
    public void testBuilder_RequireObjectMapperFromConverterFactory() {
        assertThrows(NullPointerException.class, () -> ZodHttpClientFactory.builder()
            .baseUrl("base-url")
            .config(new HttpConfig())
            .callEngine(mock(CallEngine.class))
            .converterFactory(mock(ConverterFactory.class))
            .build());
    }

    @Test
    public void testBuilder_RequireCallEngine() {
        assertThrows(NullPointerException.class, () -> ZodHttpClientFactory.builder()
            .baseUrl("base-url")
            .config(new HttpConfig())
            .callEngine(null)
            .converterFactory(mock(ConverterFactory.class))
            .build());
    }

    @Test
    public void testBuilder() {
        ZodHttpClientFactory factory = ZodHttpClientFactory.builder()
            .baseUrl("base-url")
            .config(new HttpConfig())
            .callEngine(mock(CallEngine.class))
            .converterFactory(m_cf)
            .logger(mock(AdvancedLogger.class))
            .build();

        assertNotNull(factory);
        assertEquals("base-url", factory.baseUrl());
        assertNotNull(factory.stringConverter());
        assertNotNull(factory.getCallEngine());
        assertNotNull(factory.logger());
        assertNotNull(factory.config());

        factory.getObjectMapper();
        factory.requestBodyConverter(mock(Type.class));
        factory.responseBodyConverter(mock(Type.class));

        verify(m_cf, times(2)).getObjectMapper();
        verify(m_cf).requestBodyConverter(any());
        verify(m_cf).responseBodyConverter(any());
        verifyNoMoreInteractions(m_cf);
    }

    @Test
    public void testCreateService_ButItDoesHaveRestClientAnnotation() {
        ZodHttpClientFactory factory = ZodHttpClientFactory.builder()
            .baseUrl("base-url")
            .config(new HttpConfig())
            .callEngine(mock(CallEngine.class))
            .converterFactory(m_cf)
            .logger(mock(AdvancedLogger.class))
            .build();

        Exception exp = assertThrows(IllegalArgumentException.class,
            () -> factory.create(TestClientWithoutRestClient.class));
        assertEquals("Only support for type that has @RestClient annotation", exp.getMessage());
    }

    @Test
    public void testCreateService_ButItIsNotAnInterface() {
        ZodHttpClientFactory factory = ZodHttpClientFactory.builder()
            .baseUrl("base-url")
            .config(new HttpConfig())
            .callEngine(mock(CallEngine.class))
            .converterFactory(m_cf)
            .logger(mock(AdvancedLogger.class))
            .build();

        Exception exp = assertThrows(IllegalArgumentException.class,
            () -> factory.create(ClassTestClient.class));
        assertEquals("API declarations must be interfaces.", exp.getMessage());
    }

    @Test
    public void testCreateService_ButItIsNull() {
        ZodHttpClientFactory factory = ZodHttpClientFactory.builder()
            .baseUrl("base-url")
            .config(new HttpConfig())
            .callEngine(mock(CallEngine.class))
            .converterFactory(m_cf)
            .logger(mock(AdvancedLogger.class))
            .build();

        Exception exp = assertThrows(NullPointerException.class,
            () -> factory.create(null));
        assertEquals("API interface must be not NULL", exp.getMessage());
    }

    @Test
    public void testCreateService() {
        ZodHttpClientFactory factory = ZodHttpClientFactory.builder()
            .baseUrl("base-url")
            .config(new HttpConfig())
            .callEngine(mock(CallEngine.class))
            .converterFactory(m_cf)
            .logger(mock(AdvancedLogger.class))
            .build();

        TestClient testClient = factory.create(TestClient.class);
        assertNotNull(testClient);
    }

}

@RestClient(category = "testClient")
interface TestClient {

}

interface TestClientWithoutRestClient {

}

class ClassTestClient {

}