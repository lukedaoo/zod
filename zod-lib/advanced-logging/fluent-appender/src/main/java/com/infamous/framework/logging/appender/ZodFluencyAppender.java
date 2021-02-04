package com.infamous.framework.logging.appender;

import com.infamous.framework.common.SystemPropertyUtils;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.status.StatusLogger;
import org.komamitsu.fluency.Fluency;

@Plugin(name = "ZodFluency", category = "Core", elementType = "appender", printObject = true)
public final class ZodFluencyAppender extends AbstractAppender {

    private static final StatusLogger LOG = StatusLogger.getLogger();
    private final SimpleDateFormat ISO8601_Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static AtomicInteger COUNTER = new AtomicInteger();
    private static final String CONTAINER_ID;
    private static final String CONTAINER_NAME;

    static {
        CONTAINER_ID = SystemPropertyUtils.getInstance()
            .getProperty("CONTAINER_ID", null);

        CONTAINER_NAME = SystemPropertyUtils.getInstance()
            .getProperty("APP_NAME", null);
    }

    private Fluency m_fluency;
    private Map<String, Object> m_parameters;
    private Map<String, String> m_staticFields;

    private ZodFluencyAppender(final String name, final Map<String, Object> parameters,
                               final Map<String, String> staticFields,
                               final LogServer[] servers, final FluencyConfig fluencyConfig, final Filter filter,
                               final Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions, null);

        this.m_parameters = new HashMap<>(parameters);
        this.m_staticFields = new HashMap<>(staticFields);

        try {
            this.m_fluency = FluencyConfig.makeFluency(servers, fluencyConfig);
            LOG.info("ZodFluencyAppender initialized");
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void append(LogEvent logEvent) {
        String level = logEvent.getLevel().name();
        String loggerName = logEvent.getLoggerName();
        String message = new String(this.getLayout().toByteArray(logEvent));
        long eventTimeAsLong = logEvent.getTimeMillis();
        Date eventTime = new Date(eventTimeAsLong);
        Map<String, String> context = logEvent.getContextData().toMap();

        Map<String, Object> m = new HashMap<>();
        if (CONTAINER_NAME != null) {
            m.put("category", CONTAINER_NAME);
        }
        if (CONTAINER_ID != null) {
            m.put("container-id", CONTAINER_ID);
        }
        m.put("level", level);
        m.put("class", loggerName);
        m.put("message", message);
        m.put("thread", logEvent.getThreadName());
        m.put("@timestamp", ISO8601_Format.format(eventTime));
        m.putAll(m_staticFields);
        context.forEach(m::put);

        try {
            // the tag is required for further processing within fluentd,
            // otherwise we would have no way to manipulate messages in transit
            this.m_fluency.emit((String) m_parameters.get("tag"), eventTimeAsLong, m);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    @PluginFactory
    public static ZodFluencyAppender createAppender(@PluginAttribute("name") final String name,
                                                    @PluginAttribute("ignoreExceptions") final String ignore,
                                                    @PluginAttribute("tag") final String tag,
                                                    @PluginElement("StaticField") final StaticField[] staticFields,
                                                    @PluginElement("Server") final LogServer[] servers,
                                                    @PluginElement("FluencyConfig") final FluencyConfig fluencyConfig,
                                                    @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                    @PluginElement("Filter") final Filter filter) {
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);

        Map<String, Object> parameters = new HashMap<>() {{
            put("tag", tag == null ? "logs" : tag);
        }};
        Map<String, String> fields = new HashMap<>(staticFields.length);

        for (StaticField field : staticFields) {
            fields.put(field.getName(), field.getValue());
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        return new ZodFluencyAppender(name, parameters, fields, servers, fluencyConfig, filter,
            layout, ignoreExceptions);
    }

}
