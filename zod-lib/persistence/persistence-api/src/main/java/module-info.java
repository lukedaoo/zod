module zod.persistence.api {
    requires static lombok;
    requires java.naming;

    requires java.transaction;
    requires java.persistence;

    requires org.hibernate.orm.hikaricp;
    requires com.zaxxer.hikari;

    requires zod.logging.app;

    exports com.infamous.framework.persistence.tx;
    exports com.infamous.framework.persistence.dao;
    exports com.infamous.framework.persistence;

    opens com.infamous.framework.persistence.tx;
    opens com.infamous.framework.persistence.dao;
    opens com.infamous.framework.persistence;
}