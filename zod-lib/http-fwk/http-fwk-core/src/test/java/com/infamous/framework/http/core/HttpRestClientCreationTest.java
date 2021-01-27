package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.HttpMethod;
import org.junit.jupiter.api.Test;

class HttpRestClientCreationTest {

    private HttpConfig m_config = mock(HttpConfig.class);

    @Test
    void testGetInstance() {

        HttpRestClientCreation instance = HttpRestClientCreation.getInstance(m_config);
        assertNotNull(instance);
    }

    @Test
    void testGetInstanceWithNewConfig() {
        HttpRestClientCreation instanceRaw = HttpRestClientCreation.getInstance(m_config);
        HttpRestClientCreation newInstance = HttpRestClientCreation.getInstance(new HttpConfig());

        assertNotEquals(instanceRaw, newInstance);
        verify(m_config).shutdown();
    }

    @Test
    void testCreateRestClient() {
        HttpRestClientCreation instance = HttpRestClientCreation.getInstance(m_config);
        HttpRequest request = instance
            .create("http://localhost:8080", "/upload", HttpMethod.GET, mock(ObjectMapper.class));
        assertNotNull(request);
    }
}