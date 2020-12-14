module zod.ftpserver.app {
    requires spring.boot;
    requires spring.context;
    requires spring.beans;
    requires spring.orm;
    requires org.apache.logging.log4j;
    requires ftpserver.core;
    requires ftplet.api;

    requires zod.persistence.api;
    requires java.transaction;
    requires java.annotation;

    requires zod.logging.app;
    requires zod.ftpserver.api;
    requires zod.file.service;
    requires zod.spring.jpa;
    requires zod.sensitive.api;
    requires zod.sensitive.app;

    exports com.infamous.zod.ftp.server;
    exports com.infamous.zod.ftp.um.config;

    opens com.infamous.zod.ftp.um.impl;
    opens com.infamous.zod.ftp.server;
    opens com.infamous.zod.ftp.um.config;
}