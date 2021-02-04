package com.infamous.framework.logging.appender;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.komamitsu.fluency.Fluency;
import org.komamitsu.fluency.fluentd.FluencyBuilderForFluentd;

class FluencyConfigTest {


    @org.junit.jupiter.api.Test
    void test() {
        FluencyConfig config =
            FluencyConfig.createFluencyConfig(false,
                "file/backup",
                32,
                32, 50l,
                5, 20, 200, 3);

        assertNotNull(config);
        FluencyBuilderForFluentd fluencyConfig = config.configure();
        assertNotNull(fluencyConfig);
    }

    @Test
    public void testMakeFluency() throws IOException {
        Fluency f1 = FluencyConfig.makeFluency(null, null);
        assertNotNull(f1);

        Fluency f2 = FluencyConfig.makeFluency(
            Collections.singletonList(LogServer.createServer("localhost", 3000)).toArray(new LogServer[0])
            , null);

        assertNotNull(f2);

        FluencyConfig config = mock(FluencyConfig.class);
        when(config.configure()).thenReturn(new FluencyBuilderForFluentd());
        Fluency f3 = FluencyConfig.makeFluency(
            Collections.singletonList(LogServer.createServer("localhost", 3000)).toArray(new LogServer[0])
            , config);

        assertNotNull(f3);
    }
}