package com.infamous.zod.storage;

import com.infamous.zod.storage.endpoint.StorageFileEndPointV1;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageServiceRestConfig extends ResourceConfig {

    public StorageServiceRestConfig() {
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
        register(CORSFilter.class);
        register(JacksonFeature.class);
        register(MultiPartFeature.class);
        register(StorageFileEndPointV1.class);
    }
}
