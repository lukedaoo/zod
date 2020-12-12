package com.infamous.zod.base.common.service;

import java.util.Locale;

public interface MessageSourceService {

    void useBaseName(String baseName);

    String getMessage(String code, Locale locale, String... params);

    default String getMessage(String code, String... params) {
        return getMessage(code, Locale.US, params);
    }
}
