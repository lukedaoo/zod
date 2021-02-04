package com.infamous.framework.logging.appender;

import com.infamous.framework.common.SystemPropertyUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;
import org.komamitsu.fluency.Fluency;
import org.komamitsu.fluency.fluentd.FluencyBuilderForFluentd;

@Plugin(name = "FluencyConfig", category = Node.CATEGORY, printObject = true)
public class FluencyConfig {

    private static final StatusLogger LOG = StatusLogger.getLogger();

    private boolean m_ackResponseMode;
    private String m_fileBackupDir;
    private int m_bufferChunkInitialSize;
    private int m_bufferChunkRetentionSize;
    private Long m_maxBufferSize;
    private int m_waitUntilBufferFlushed;
    private int m_waitUntilFlusherTerminated;
    private int m_flushIntervalMillis;
    private int m_senderMaxRetryCount;

    private FluencyConfig() {
    }

    public FluencyBuilderForFluentd configure() {
        FluencyBuilderForFluentd config = new FluencyBuilderForFluentd();
        config.setAckResponseMode(m_ackResponseMode);
        if (m_fileBackupDir != null) {
            config.setFileBackupDir(m_fileBackupDir);
        }
        if (m_bufferChunkInitialSize > 0) {
            config.setBufferChunkInitialSize(m_bufferChunkInitialSize);
        }
        if (m_bufferChunkRetentionSize > 0) {
            config.setBufferChunkRetentionSize(m_bufferChunkRetentionSize);
        }
        if (m_maxBufferSize > 0) {
            config.setMaxBufferSize(m_maxBufferSize);
        }
        if (m_waitUntilBufferFlushed > 0) {
            config.setWaitUntilBufferFlushed(m_waitUntilBufferFlushed);
        }
        if (m_waitUntilFlusherTerminated > 0) {
            config.setWaitUntilFlusherTerminated(m_waitUntilFlusherTerminated);
        }
        if (m_flushIntervalMillis > 0) {
            config.setFlushAttemptIntervalMillis(m_flushIntervalMillis);
        }
        if (m_senderMaxRetryCount > 0) {
            config.setSenderMaxRetryCount(m_senderMaxRetryCount);
        }
        return config;
    }


    // These are the defaults, it no configuration is given:
    // Single Fluentd(localhost:24224 by default)
    //   - TCP heartbeat (by default)
    //   - Asynchronous flush (by default)
    //   - Without ack response (by default)
    //   - Flush interval is 600ms (by default)
    //   - Initial chunk buffer size is 1MB (by default)
    //   - Threshold chunk buffer size to flush is 4MB (by default)
    //   - Max total buffer size is 16MB (by default)
    //   - Max retry of sending events is 8 (by default)
    //   - Max wait until all buffers are flushed is 60 seconds (by default)
    //   - Max wait until the flusher is terminated is 60 seconds (by default)
    public static Fluency makeFluency(LogServer[] servers, FluencyConfig config) throws IOException {
        if ((servers == null || servers.length == 0) && config == null) {
            return makeDefaultFluency();
        }
        if (servers == null || servers.length == 0) {
            return makeDefaultFluency(config);
        }
        List<InetSocketAddress> addresses = new ArrayList<>(servers.length);
        for (LogServer s : servers) {
            addresses.add(s.configure());
        }
        LOG.info("Connect to log server " + Arrays.toString(servers));
        if (config == null) {
            return new FluencyBuilderForFluentd().build(addresses);
        }
        return config.configure().build(addresses);
    }

    private static Fluency makeDefaultFluency() {
        return makeDefaultFluency(null);
    }

    private static Fluency makeDefaultFluency(FluencyConfig config) {
        String defaultHost = SystemPropertyUtils.getInstance()
            .getProperty("fluent.host", "localhost");
        int defaultPort = SystemPropertyUtils.getInstance()
            .getAsInt("fluent.port", 514);
        LogServer logServer = LogServer.createServer(defaultHost, defaultPort);
        LOG.info("Connect to log server " + logServer);
        InetSocketAddress address = logServer.configure();
        List<InetSocketAddress> addresses = Collections.singletonList(address);

        return config == null
            ? new FluencyBuilderForFluentd().build()
            : config.configure().build(addresses);
    }

    @PluginFactory
    public static FluencyConfig createFluencyConfig(
        @PluginAttribute("ackResponseMode") final boolean ackResponseMode,
        @PluginAttribute("fileBackupDir") final String fileBackupDir,
        @PluginAttribute("bufferChunkInitialSize") final int bufferChunkInitialSize,
        @PluginAttribute("bufferChunkRetentionSize") final int bufferChunkRetentionSize,
        @PluginAttribute("maxBufferSize") final Long maxBufferSize,
        @PluginAttribute("waitUntilBufferFlushed") final int waitUntilBufferFlushed,
        @PluginAttribute("waitUntilFlusherTerminated") final int waitUntilFlusherTerminated,
        @PluginAttribute("flushIntervalMillis") final int flushIntervalMillis,
        @PluginAttribute("senderMaxRetryCount") final int senderMaxRetryCount) {
        FluencyConfig config = new FluencyConfig();
        config.m_ackResponseMode = ackResponseMode;
        config.m_fileBackupDir = fileBackupDir;
        config.m_bufferChunkInitialSize = bufferChunkInitialSize;
        config.m_bufferChunkRetentionSize = bufferChunkRetentionSize;
        config.m_maxBufferSize = maxBufferSize;
        config.m_waitUntilBufferFlushed = waitUntilBufferFlushed;
        config.m_waitUntilFlusherTerminated = waitUntilFlusherTerminated;
        config.m_flushIntervalMillis = flushIntervalMillis;
        config.m_senderMaxRetryCount = senderMaxRetryCount;
        return config;
    }

    @Override
    public String toString() {
        return "FluencyConfig{" +
            "ackResponseMode=" + m_ackResponseMode +
            ", fileBackupDir='" + m_fileBackupDir + '\'' +
            ", bufferChunkInitialSize=" + m_bufferChunkInitialSize +
            ", bufferChunkRetentionSize=" + m_bufferChunkRetentionSize +
            ", maxBufferSize=" + m_maxBufferSize +
            ", waitUntilBufferFlushed=" + m_waitUntilBufferFlushed +
            ", waitUntilFlusherTerminated=" + m_waitUntilFlusherTerminated +
            ", flushIntervalMillis=" + m_flushIntervalMillis +
            ", senderMaxRetryCount=" + m_senderMaxRetryCount +
            '}';
    }
}
