package com.infamous.zod.songmgmt;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.base.rest.JerseyRestConfig;
import com.infamous.zod.base.rest.ProxyEndpointContext;
import com.infamous.zod.base.rest.ProxyEndpointFactory;
import com.infamous.zod.songmgmt.controller.SongManagementController;
import com.infamous.zod.songmgmt.endpoint.SongManagementEndpointV1;
import com.infamous.zod.songmgmt.endpoint.impl.SongManagementEndpointV1Impl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SongManagementRestConfig {

    @Bean
    public SongManagementEndpointV1 createStorageFileEndpoint(SongManagementController controller) {
        ZodLogger logger = ZodLoggerUtil.getLogger(SongManagementEndpointV1.class, "song.mgmt");
        ProxyEndpointContext<SongManagementEndpointV1> context
            = new ProxyEndpointContext.ProxyEndpointContextBuilder<SongManagementEndpointV1>()
            .anInterface(SongManagementEndpointV1.class)
            .target(new SongManagementEndpointV1Impl(controller))
            .logger(logger)
            .postInit(endpoint -> logger.info("Register SongManagementEndpoint - /song-management/v1"))
            .build();
        return ProxyEndpointFactory.getInstance().create(context);
    }

    @Bean
    public JerseyRestConfig restConfig(SongManagementEndpointV1 songManagementEndpointV1) {
        return new JerseyRestConfig((config) -> {
            config.register(songManagementEndpointV1, SongManagementEndpointV1.class);
        });
    }
}
