package com.infamous.zod.base.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.infamous.framework.file.FileService;
import com.infamous.zod.base.common.service.MessageSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommonServiceConfigTest {


    private CommonServiceConfig m_config;

    @BeforeEach
    public void setup() {
        m_config = new CommonServiceConfig();
    }

    @Test
    public void testCreateFileService() {
        FileService fileService = m_config.createCoreFileService();
        assertNotNull(fileService);
    }

    @Test
    public void testCreateMessageSourceService() {
        MessageSourceService messageSourceService = m_config.createMessageSourceService();
        assertNotNull(messageSourceService);
    }
}