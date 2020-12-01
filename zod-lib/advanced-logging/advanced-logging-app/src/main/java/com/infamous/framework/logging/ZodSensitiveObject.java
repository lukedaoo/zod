package com.infamous.framework.logging;

import com.infamous.framework.sensitive.DefaultSensitiveObject;
import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.core.SensitiveHashingService;
import com.infamous.framework.sensitive.core.SensitiveObject;
import java.util.Optional;

public class ZodSensitiveObject extends DefaultSensitiveObject implements SensitiveObject {

    private Optional<MessageDigestAlgorithm> m_digestAlgorithm;

    public ZodSensitiveObject(Object object) {
        this(object, MessageDigestAlgorithm.MD5);
    }

    public ZodSensitiveObject(Object object, MessageDigestAlgorithm algorithm) {
        super(object);
        m_digestAlgorithm = Optional.ofNullable(algorithm);
    }

    @Override
    public String replace() {
        String messageFromObject = String.valueOf(getObject());
        Optional<SensitiveHashingService> sensitiveDataService = ZodLoggerFactory.getInstance()
            .getSensitiveHashingService();

        if (sensitiveDataService.isEmpty()) {
            throw new IllegalStateException("Not found sensitive service");
        }
        try {
            if (m_digestAlgorithm.isPresent()) {
                return sensitiveDataService.get().hash(messageFromObject, m_digestAlgorithm.get());
            } else {
                return sensitiveDataService.get().hash(messageFromObject);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
