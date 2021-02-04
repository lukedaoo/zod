package com.infamous.framework.logging.appender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LogServerTest {


    @Test
    public void test() {
        LogServer server = LogServer.createServer("localhost", 32);

        assertNotNull(server);
        assertEquals("localhost", server.configure().getHostName());
        assertEquals(32, server.configure().getPort());
    }

    @Test
    public void testInvalid_Input() {
        assertThrows(IllegalArgumentException.class, () -> LogServer.createServer(null, 32));
        assertThrows(IllegalArgumentException.class, () -> LogServer.createServer("", 32));

        assertThrows(IllegalArgumentException.class, () -> LogServer.createServer("localhost", 0));
        assertThrows(IllegalArgumentException.class, () -> LogServer.createServer("localhost", -1));
    }
}