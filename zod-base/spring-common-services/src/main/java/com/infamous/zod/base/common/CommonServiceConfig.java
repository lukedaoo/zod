package com.infamous.zod.base.common;

import com.infamous.framework.file.FileService;
import com.infamous.framework.file.FileServiceImpl;
import com.infamous.zod.base.common.service.MessageSourceService;
import com.infamous.zod.base.common.service.impl.MessageSourceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

public class CommonServiceConfig {

    @Bean
    public FileService coreFileService() {
        return new FileServiceImpl();
    }

    @Bean
    @Lazy
    public MessageSourceService messageSourceService() {
        return new MessageSourceServiceImpl();
    }
}
