package com.infamous.framework.logging.appender;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.jackson.ContextDataSerializer;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

@Plugin(name = "ZodJsonLayout", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class ZodJsonLayout extends AbstractStringLayout {

    private static DateFormat ISO_8601_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");

    static {
        ISO_8601_DATE_TIME.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private final ObjectMapper objectMapper;

    ZodJsonLayout(Charset charset) {
        super(charset);
        SimpleModule module = new SimpleModule();
        module.addSerializer(LogEvent.class, new LogEventSerializer());
        module.addSerializer(Throwable.class, new ThrowableSerializer());
        module.addSerializer(ReadOnlyStringMap.class, new ContextDataSerializer() {
        });
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Override
    public String toSerializable(LogEvent logEvent) {
        try {
            return objectMapper.writeValueAsString(logEvent);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static class LogEventSerializer extends StdSerializer<LogEvent> {

        LogEventSerializer() {
            super(LogEvent.class);
        }

        @Override
        public void serialize(LogEvent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            Date date = new Date(value.getTimeMillis());
            gen.writeStringField("timestamp", ISO_8601_DATE_TIME.format(date));
            gen.writeStringField("thread", value.getThreadName());
            gen.writeStringField("level", value.getLevel().name());
            gen.writeStringField("class", value.getLoggerName());
            gen.writeObjectField("message", value.getMessage().getFormattedMessage());
            gen.writeObjectField("thrown", value.getThrown());
            gen.writeObjectField("context", value.getContextData());
            gen.writeEndObject();
        }
    }

    private static class ThrowableSerializer extends StdSerializer<Throwable> {

        ThrowableSerializer() {
            super(Throwable.class);
        }

        @Override
        public void serialize(Throwable value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            try (StringWriter stringWriter = new StringWriter()) {
                try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                    value.printStackTrace(printWriter);
                    gen.writeString(stringWriter.toString());
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    public static class Builder<B extends Builder<B>> extends
        org.apache.logging.log4j.core.layout.AbstractStringLayout.Builder<B> implements
        org.apache.logging.log4j.core.util.Builder<ZodJsonLayout> {

        Builder() {
            this.setCharset(StandardCharsets.UTF_8);
        }

        public ZodJsonLayout build() {
            return new ZodJsonLayout(this.getCharset());
        }
    }

}
