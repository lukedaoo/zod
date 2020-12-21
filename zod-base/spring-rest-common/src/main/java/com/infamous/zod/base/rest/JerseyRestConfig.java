package com.infamous.zod.base.rest;

import java.util.function.Consumer;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;

public class JerseyRestConfig extends ResourceConfig {

    public JerseyRestConfig(Consumer<ResourceConfig> configConsumer) {
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
        register(CORSFilter.class);
        register(JacksonFeature.class);
        register(MultiPartFeature.class);
        configConsumer.accept(this);
    }
}
