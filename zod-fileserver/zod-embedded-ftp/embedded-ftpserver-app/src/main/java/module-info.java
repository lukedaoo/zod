module embedded.ftpserver.app {
    requires spring.context;
    requires spring.beans;
    requires org.apache.logging.log4j;
    requires ftpserver.core;
    requires ftplet.api;

    requires persistence.api;
    requires java.transaction;

    requires advanced.logging.app;
    requires embedded.ftpserver.api;
    requires spring.orm;
    requires spring.jpa.autoconfig;

    exports com.infamous.zod.ftp.server;
    exports com.infamous.zod.ftp.um.config;

    opens com.infamous.zod.ftp.um.impl;
    opens com.infamous.zod.ftp.server;
}