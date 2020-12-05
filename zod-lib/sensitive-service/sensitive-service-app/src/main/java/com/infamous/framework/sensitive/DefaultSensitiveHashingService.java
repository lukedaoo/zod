package com.infamous.framework.sensitive;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.core.SensitiveHashingService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class DefaultSensitiveHashingService implements SensitiveHashingService {

    public DefaultSensitiveHashingService() {

    }

    @Override
    public String hash(String str, MessageDigestAlgorithm algorithm) {
        return getMessageDigest(algorithm.getName())
            .map(md -> buildHashString(md, str))
            .orElse(str);
    }

    private String buildHashString(MessageDigest md, String str) {
        md.update(str.getBytes(), 0, str.length());
        return new BigInteger(1, md.digest()).toString(16);
    }

    private Optional<MessageDigest> getMessageDigest(String s) {
        try {
            return Optional.of(MessageDigest.getInstance(s));
        } catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }
}
