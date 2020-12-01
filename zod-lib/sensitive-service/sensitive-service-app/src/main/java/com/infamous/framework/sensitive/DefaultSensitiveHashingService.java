package com.infamous.framework.sensitive;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.core.SensitiveHashingService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DefaultSensitiveHashingService implements SensitiveHashingService {

    public DefaultSensitiveHashingService() {

    }

    @Override
    public String hash(String str, MessageDigestAlgorithm algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = getMessageDigest(algorithm.getName());

        md.update(str.getBytes(), 0, str.length());
        return new BigInteger(1, md.digest()).toString(16);
    }

    private MessageDigest getMessageDigest(String s) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(s);
    }
}
