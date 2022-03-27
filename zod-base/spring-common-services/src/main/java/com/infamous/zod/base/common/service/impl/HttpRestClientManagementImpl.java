package com.infamous.zod.base.common.service.impl;

import com.infamous.framework.factory.JacksonConverterFactory;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.engine.JavaHttpEngine;
import com.infamous.framework.http.factory.ZodHttpClientFactory;
import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.base.common.service.HttpRestClientManagement;
import com.infamous.zod.base.common.service.RestDetectorService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;

public class HttpRestClientManagementImpl implements HttpRestClientManagement {

    private static final ZodLogger LOGGER = ZodLoggerUtil
        .getLogger(HttpRestClientManagementImpl.class, "spring.common");

    private Map<String, ZodHttpClientFactory> m_cache;
    private RestDetectorService m_restDetectorService;

    @Autowired
    public HttpRestClientManagementImpl(RestDetectorService detectorService) {
        m_cache = new ConcurrentHashMap<>(5);
        m_restDetectorService = detectorService;
    }

    @Override
    public <T> T newRestClient(String serviceName, Class<T> restClientInterface) {
        if (m_cache.containsKey(serviceName)) {
            return m_cache.get(serviceName).create(restClientInterface);
        }
        ZodHttpClientFactory factory = createFactory(serviceName);
        m_cache.put(serviceName, factory);
        return m_cache.get(serviceName).create(restClientInterface);
    }

    private String findUrl(String serviceName) {
        try {
            return m_restDetectorService.findUri(serviceName);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
        return null;
    }

    private ZodHttpClientFactory createFactory(String serviceName) {
        String url = findUrl(serviceName);
        if (url == null) {
            return null;
        }
        return newHttpClientFactory(url);
    }

    private ZodHttpClientFactory newHttpClientFactory(String baseUrl) {
        return ZodHttpClientFactory.builder()
            .baseUrl(baseUrl)
            .converterFactory(JacksonConverterFactory.create())
            .callEngine(JavaHttpEngine.getInstance())
            .config(new HttpConfig())
            .build();
    }
}
