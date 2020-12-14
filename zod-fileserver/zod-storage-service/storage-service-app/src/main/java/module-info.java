module zod.storage.app {
    requires spring.context;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.orm;

    requires java.sql;
    requires java.annotation;

    requires net.bytebuddy;
    requires com.fasterxml.classmate;
    requires java.transaction;

    requires org.apache.logging.log4j;

    requires zod.ftpserver.api;
    requires zod.ftpserver.app;
    requires zod.logging.app;
    requires zod.jdbc.mariadb.client;
    requires zod.persistence.api;
    requires zod.storage.api;
    requires zod.file.service;
    requires zod.spring.common;
    requires zod.spring.jpa;

    opens com.infamous.zod.storage;
}