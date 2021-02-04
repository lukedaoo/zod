package com.infamous.framework.logging.appender;

import java.net.InetSocketAddress;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "Server", category = Node.CATEGORY, printObject = true)
public class LogServer {

    private final String m_host;
    private final int m_port;

    private LogServer(final String host, final int port) {
        this.m_host = host;
        this.m_port = port;
    }

    public InetSocketAddress configure() {
        return new InetSocketAddress(m_host, m_port);
    }

    @PluginFactory
    public static LogServer createServer(@PluginAttribute("host") final String host,
                                         @PluginAttribute("port") final int port) {
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Host is required");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("Port is required");
        }
        return new LogServer(host, port);
    }

    @Override
    public String toString() {
        return "LogServer{" +
            "host='" + m_host + '\'' +
            ", port=" + m_port +
            '}';
    }
}
