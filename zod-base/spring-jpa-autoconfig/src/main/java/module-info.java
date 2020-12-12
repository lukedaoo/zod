module zod.spring.jpa {

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

    requires zod.persistence.api;
    requires zod.system.property.utils;
    requires zod.logging.app;

    exports com.infamous.zod.base.jpa;
    opens com.infamous.zod.base.jpa;
}