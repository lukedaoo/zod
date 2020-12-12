package com.infamous.framework.sensitive.core;

import java.util.Optional;

public interface SensitiveHashingService {

    default String hash(String obj, MessageDigestAlgorithm algorithm) {
        return Optional.ofNullable(obj)
            .map(s -> doHash(s, algorithm))
            .orElse("");
    }

    private String doHash(String obj, MessageDigestAlgorithm algorithm) {
        return Optional.ofNullable(algorithm)
            .map(al -> al.hash(obj))
            .orElse(obj);
    }
}