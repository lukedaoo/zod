package com.infamous.framework.logging.appender;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.util.Constants;

@Plugin(name = "ZodSocket", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class ZodSocketAppender extends SocketAppender {

    protected ZodSocketAppender(String name, Layout<? extends Serializable> layout,
                                Filter filter,
                                AbstractSocketManager manager, boolean ignoreExceptions,
                                boolean immediateFlush, Advertiser advertiser,
                                Property[] properties) {
        super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, properties);
    }

    @PluginBuilderFactory
    public static <B extends ZodSocketAppenderBuilder<B>> B newAppenderBuilder() {
        return new ZodSocketAppenderBuilder<B>().asBuilder();
    }


    public static class ZodSocketAppenderBuilder<B extends ZodSocketAppenderBuilder<B>> extends AbstractBuilder<B>
        implements org.apache.logging.log4j.core.util.Builder<SocketAppender> {

        @SuppressWarnings({"resource", "unchecked"})
        @Override
        public synchronized ZodSocketAppender build() {
            final Protocol protocol = getProtocol();
            final SslConfiguration sslConfiguration = getSslConfiguration();
            final Configuration configuration = getConfiguration();
            Layout<? extends Serializable> layout = getLayout();
            if (layout == null) {
                layout = new ZodJsonLayout(StandardCharsets.UTF_8);
            }
            final String name = getName();
            if (name == null) {
                LOGGER.error("No name provided for ZodSocket");
                return null;
            }
            LOGGER.info("Send log to " + getHost() + ":" + getPort());
            final AbstractSocketManager manager = createSocketManager(name, protocol, getHost(), getPort(),
                getConnectTimeoutMillis(),
                sslConfiguration, getReconnectDelayMillis(), getImmediateFail(), layout,
                Constants.ENCODER_BYTE_BUFFER_SIZE,
                null);

            return new ZodSocketAppender(name, layout, getFilter(), manager, isIgnoreExceptions(), isImmediateFlush(),
                getAdvertise() ? configuration.getAdvertiser() : null, null);
        }
    }

    @Override
    protected void directEncodeEvent(LogEvent event) {
        super.directEncodeEvent(event);
    }
}
