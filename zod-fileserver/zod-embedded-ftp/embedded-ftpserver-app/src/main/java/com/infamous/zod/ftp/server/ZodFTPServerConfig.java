package com.infamous.zod.ftp.server;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.ftp.FTPServerConfigProperties;
import java.util.Collections;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

public class ZodFTPServerConfig {

    private static final ZodLogger LOGGER = ZodLoggerUtil
        .getLogger(ZodFTPServerConfig.class, "ftp.server");

    @Bean
    public FTPServerConfigProperties configProperties() {
        return new FTPServerConfigProperties();
    }

    @Bean
    public FileSystemFactory createFileSystemFactory() {
        NativeFileSystemFactory systemFactory = new NativeFileSystemFactory();
        systemFactory.setCaseInsensitive(false);
        systemFactory.setCreateHome(true);
        return systemFactory;
    }

    @Bean
    public Listener nioListener(@Value("${ftp.port:2121}") int port) {
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);
        DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
        dataConnectionConfigurationFactory.setPassiveIpCheck(false);
        dataConnectionConfigurationFactory.setIdleTime(1000);
        factory.setDataConnectionConfiguration(dataConnectionConfigurationFactory.createDataConnectionConfiguration());
        LOGGER.info("FTP Server is running at port: " + port);
        return factory.createListener();
    }

    @Bean(name = "ftpServer")
    public FtpServer server(FileSystemFactory fileSystemFactory, Listener listener, UserManager um) {
        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.setFileSystem(fileSystemFactory);
        serverFactory.setListeners(Collections.singletonMap("default", listener));
        serverFactory.setUserManager(um);
        //serverFactory.setCommandFactory(cf);
        return serverFactory.createServer();
    }

    @Bean
    public InitializingBean start(FtpServer server) {
        return server::start;
    }

    @Bean
    public DisposableBean stop(FtpServer server) {
        return server::stop;
    }
}
