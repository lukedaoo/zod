package com.infamous.framework.logging;

import com.infamous.framework.sensitive.DefaultSensitiveHashingService;
import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.core.SensitiveHashingService;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZodSensitiveHashingService extends DefaultSensitiveHashingService implements SensitiveHashingService {

    private AtomicBoolean m_includedRawString = new AtomicBoolean(false);
    private static final String SEPARATOR = "####";


    public ZodSensitiveHashingService() {
        enabledIncludingRawValue();
    }

    @Override
    public String hash(String str, MessageDigestAlgorithm algorithm){
        return buildResult(str, super.hash(str, algorithm));
    }

    public boolean isIncludedRawValue() {
        return m_includedRawString.get();
    }

    public void disabledIncludingRawValue() {
        m_includedRawString.getAndSet(false);
    }

    public void enabledIncludingRawValue() {
        m_includedRawString.getAndSet(true);
    }

    private String buildResult(String actual, String hashStr) {
        StringBuilder builder = new StringBuilder();
        if (isIncludedRawValue()) {
            builder.append(SEPARATOR).append(actual).append(SEPARATOR).append(hashStr).append(SEPARATOR);
        } else {
            builder.append(SEPARATOR).append(hashStr).append(SEPARATOR);
        }
        return builder.toString();
    }

}
