module spring.jpa.autoconfig {

    requires spring.context;
    requires spring.orm;
    requires spring.boot.autoconfigure;
    requires spring.tx;
    requires spring.beans;

    requires transactions.jta;
    requires transactions.api;

    requires java.xml.bind;
    requires java.persistence;
    requires java.sql;

    requires persistence.api;
    requires system.property.utils;
    requires advanced.logging.app;

    exports com.infamous.zod.base.jpa;
    opens com.infamous.zod.base.jpa;
}