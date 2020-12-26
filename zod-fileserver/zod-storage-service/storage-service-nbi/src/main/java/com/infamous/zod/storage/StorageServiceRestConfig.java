package com.infamous.zod.storage;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.base.rest.JerseyRestConfig;
import com.infamous.zod.base.rest.ProxyEndpointContext;
import com.infamous.zod.base.rest.ProxyEndpointFactory;
import com.infamous.zod.media.streaming.controller.MediaStreamingController;
import com.infamous.zod.media.streaming.endpoint.MediaStreamingEndPointV1;
import com.infamous.zod.media.streaming.endpoint.impl.MediaStreamingEndPointV1Impl;
import com.infamous.zod.storage.controller.StorageFileController;
import com.infamous.zod.storage.endpoint.StorageFileEndPointV1;
import com.infamous.zod.storage.endpoint.impl.StorageFileEndPointV1Impl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageServiceRestConfig {

    @Bean
    public StorageFileEndPointV1 createStorageFileEndpoint(StorageFileController controller) {
        ZodLogger logger = ZodLoggerUtil.getLogger(StorageFileEndPointV1.class, "storage.service");
        ProxyEndpointContext<StorageFileEndPointV1> context
            = new ProxyEndpointContext.ProxyEndpointContextBuilder<StorageFileEndPointV1>()
            .anInterface(StorageFileEndPointV1.class)
            .target(new StorageFileEndPointV1Impl(controller))
            .logger(logger)
            .postInit(endpoint -> logger.info("Register StorageFileEndPoint - /storage/v1"))
            .build();
        return ProxyEndpointFactory.getInstance().create(context);
    }

    @Bean
    public MediaStreamingEndPointV1 createMediaStreamingEndpoint(MediaStreamingController controller) {
        ZodLogger logger = ZodLoggerUtil.getLogger(MediaStreamingEndPointV1.class, "media.streaming");
        ProxyEndpointContext<MediaStreamingEndPointV1> context
            = new ProxyEndpointContext.ProxyEndpointContextBuilder<MediaStreamingEndPointV1>()
            .anInterface(MediaStreamingEndPointV1.class)
            .target(new MediaStreamingEndPointV1Impl(controller))
            .logger(logger)
            .postInit(endpoint -> logger.info("Register MediaStreamingEndPoint - /view/v1"))
            .build();
        return ProxyEndpointFactory.getInstance().create(context);
    }

    @Bean
    public JerseyRestConfig restConfig(StorageFileEndPointV1 storageFileEP, MediaStreamingEndPointV1 mediaStreamingEP) {
        return new JerseyRestConfig((config) -> {
            config.register(storageFileEP, StorageFileEndPointV1.class);
            config.register(mediaStreamingEP, MediaStreamingEndPointV1.class);
        });
    }
}
