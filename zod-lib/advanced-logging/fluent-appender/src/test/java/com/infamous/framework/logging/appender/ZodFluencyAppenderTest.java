package com.infamous.framework.logging.appender;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.komamitsu.fluency.Fluency;
import org.komamitsu.fluency.fluentd.FluencyBuilderForFluentd;
import org.mockito.ArgumentMatcher;

class ZodFluencyAppenderTest {

    private FluencyConfig m_config;
    private Fluency m_fluency;

    @BeforeEach
    public void setup() {
        m_config = mock(FluencyConfig.class);
        m_fluency = mock(Fluency.class);

        FluencyBuilderForFluentd builderForFluentd = mock(FluencyBuilderForFluentd.class);

        when(m_config.configure()).thenReturn(builderForFluentd);
        when(builderForFluentd.build(any(List.class))).thenReturn(m_fluency);
    }

    @Test
    public void testCreate() throws IOException {
        ZodFluencyAppender appender = ZodFluencyAppender.createAppender("appenderName",
            "false", "zod",
            Collections.singletonList(StaticField.createStaticField("application", "testing"))
                .toArray(new StaticField[0]),
            new LogServer[0], m_config, null, null);

        assertNotNull(appender);

        appender.append(mockLogEvent());

        Map<String, Object> map = new HashMap<>();
        map.put("level", "INFO");
        map.put("thread", null);
        map.put("message", "\n");
        map.put("class", "test.logging");
        map.put("application", "testing");

        verify(m_fluency).emit(eq("zod"), eq(1000L),
            argThat((ArgumentMatcher<Map<String, Object>>) map1 ->
                map1.get("@timestamp") != null
                    && map1.get("level").equals("INFO")
                    && map1.get("thread") == null
                    && map1.get("message").equals("\n")
                    && map1.get("class").equals("test.logging")
                    && map1.get("application").equals("testing")));
    }

    private LogEvent mockLogEvent() {
        LogEvent logEvent = mock(LogEvent.class);
        when(logEvent.getLevel()).thenReturn(Level.INFO);
        when(logEvent.getLoggerName()).thenReturn("test.logging");
        when(logEvent.getTimeMillis()).thenReturn(1000l);
        when(logEvent.getContextData()).thenReturn(new DefaultThreadContextMap());
        return logEvent;
    }
}