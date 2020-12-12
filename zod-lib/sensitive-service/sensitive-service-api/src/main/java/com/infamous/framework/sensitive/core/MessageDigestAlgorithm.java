package com.infamous.framework.sensitive.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public enum MessageDigestAlgorithm {

    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_256("SHA-256"),
    SHA_512("SHA-512");

    private final String m_name;

    MessageDigestAlgorithm(String name) {
        this.m_name = name;
    }

    public String getName() {
        return m_name;
    }

    public String hash(String source) {
        return getMessageDigest(this.getName())
            .map(md -> HashStringUtils.hash(md, source))
            .orElseThrow(() -> new IllegalStateException("Not supported message digest algorithm"));
    }

    private Optional<MessageDigest> getMessageDigest(String s) {
        try {
            return Optional.of(MessageDigest.getInstance(s));
        } catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }
}
