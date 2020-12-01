package com.infamous.framework.sensitive.core;

import java.util.Arrays;

public enum MessageDigestAlgorithm {

    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_256("SHA-256"),
    SHA_512("SHA-512");

    private String m_name;

    MessageDigestAlgorithm(String name) {
        this.m_name = name;
    }

    public String getName() {
        return m_name;
    }

    public static MessageDigestAlgorithm from(String name) {
        return Arrays.stream(MessageDigestAlgorithm.values())
            .filter(algorithm -> algorithm.getName().equalsIgnoreCase(name))
            .findAny().orElseThrow(() -> new IllegalArgumentException("Unsupported algorithm"));
    }
}
