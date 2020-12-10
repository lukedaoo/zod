package com.infamous.framework.sensitive.core;

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
}
