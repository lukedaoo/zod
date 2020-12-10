module storage.service.app {
    requires spring.context;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.jpa.autoconfig;

    requires java.sql;

    requires net.bytebuddy;
    requires com.fasterxml.classmate;

    requires org.apache.logging.log4j;

    requires java.annotation;

    requires embedded.ftpserver.app;
    requires embedded.ftpserver.api;
    requires advanced.logging.app;
    requires mariadb.java.client.wrapper;
    requires spring.orm;
    requires persistence.api;
    requires storage.service.api;

    opens com.infamous.zod.storage;
}