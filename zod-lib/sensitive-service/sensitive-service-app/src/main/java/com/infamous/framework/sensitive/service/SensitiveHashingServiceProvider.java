package com.infamous.framework.sensitive.service;

import com.infamous.framework.sensitive.core.DefaultSensitiveHashingService;
import com.infamous.framework.sensitive.core.SensitiveHashingService;
import java.util.Objects;

public class SensitiveHashingServiceProvider {

    private SensitiveHashingService m_service = new DefaultSensitiveHashingService();
    private static final SensitiveHashingServiceProvider INSTANCE = new SensitiveHashingServiceProvider();

    private SensitiveHashingServiceProvider() {

    }

    public static SensitiveHashingServiceProvider getInstance() {
        return INSTANCE;
    }

    public synchronized void use(SensitiveHashingService service) {
        Objects.requireNonNull(service, "SensitiveHashingService is null");
        m_service = service;
    }

    public SensitiveHashingService getService() {
        return m_service;
    }
}
