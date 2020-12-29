package com.infamous.zod.ftp.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZodFTPServerConfigTest {

    private ZodFTPServerConfig m_zodFTPServerConfig;

    @BeforeEach
    void setup() {
        m_zodFTPServerConfig = new ZodFTPServerConfig();
    }

    @Test
    public void testCreateServerConfigProp() {
        assertNotNull(m_zodFTPServerConfig.createConfigProperties());
    }

    @Test
    public void testCreateNioListener() {
        Listener nioListener = m_zodFTPServerConfig.createNioListener(2121);

        assertEquals(2000, nioListener.getDataConnectionConfiguration().getIdleTime());
        assertEquals(2121, nioListener.getPort());
    }

    @Test
    public void testCreateFileSystemFactory() {
        assertNotNull(m_zodFTPServerConfig.createFileSystemFactory());
    }

    @Test
    public void testCreateServer() {
        FileSystemFactory fileSystemFactory = mock(FileSystemFactory.class);
        Listener listener = m_zodFTPServerConfig.createNioListener(1000);
        UserManager um = mock(UserManager.class);

        assertNotNull(m_zodFTPServerConfig.createServer(fileSystemFactory, listener, um));
    }
}