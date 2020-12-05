package com.infamous.framework.logging;

import com.infamous.framework.sensitive.DefaultSensitiveObject;
import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.core.SensitiveHashingService;
import com.infamous.framework.sensitive.core.SensitiveObject;

public class ZodSensitiveObject extends DefaultSensitiveObject implements SensitiveObject {

    private MessageDigestAlgorithm m_digestAlgorithm;

    public ZodSensitiveObject(Object object) {
        this(object, MessageDigestAlgorithm.MD5);
    }

    public ZodSensitiveObject(Object object, MessageDigestAlgorithm algorithm) {
        super(object);
        m_digestAlgorithm = algorithm;
    }

    @Override
    public String replace() {
        String messageFromObject = String.valueOf(getObject());
        SensitiveHashingService hashingService = ZodLoggerFactory.getInstance().getSensitiveHashingService();

        return hashingService.hash(messageFromObject, m_digestAlgorithm);
    }
}
