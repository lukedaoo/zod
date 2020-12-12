package com.infamous.zod.base.common.service.impl;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.base.common.service.MessageSourceService;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class MessageSourceServiceImpl implements MessageSourceService {

    private static final ZodLogger LOGGER = ZodLoggerUtil
        .getLogger(MessageSourceServiceImpl.class, "spring.common");

    private static final String DEFAULT_BASE_NAME = "/";
    private String m_baseName;
    private MessageSource m_realService;

    public MessageSourceServiceImpl() {
        this(DEFAULT_BASE_NAME);
    }

    public MessageSourceServiceImpl(String baseName) {
        useBaseName(baseName);
    }

    private void init() {
        ReloadableResourceBundleMessageSource messageSource
            = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename(m_baseName);
        messageSource.setDefaultEncoding("UTF-8");

        m_realService = messageSource;
        LOGGER.info("Loaded message source at " + m_baseName);
    }

    @Override
    public void useBaseName(String baseName) {
        m_baseName = baseName;
        init();
    }

    @Override
    public String getMessage(String code, Locale locale, String... params) {
        return Optional.of(m_realService.getMessage(code, params, locale))
            .orElse(code);
    }
}
