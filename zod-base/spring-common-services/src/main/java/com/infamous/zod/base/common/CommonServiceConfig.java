package com.infamous.zod.base.common;

import com.infamous.framework.file.FileService;
import com.infamous.framework.file.FileServiceImpl;
import com.infamous.zod.base.common.service.MessageSourceService;
import com.infamous.zod.base.common.service.impl.MessageSourceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

public class CommonServiceConfig {

    @Bean
    @Lazy
    public FileService createCoreFileService() {
        return new FileServiceImpl();
    }

    @Bean
    @Lazy
    public MessageSourceService createMessageSourceService() {
        return new MessageSourceServiceImpl();
    }
}
