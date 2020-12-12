module zod.storage.api {
    requires static lombok;
    requires zod.persistence.api;
    requires java.persistence;
    requires org.hibernate.orm.core;

    exports com.infamous.zod.storage.model;
    exports com.infamous.zod.storage.repository;

    opens com.infamous.zod.storage.model;
    opens com.infamous.zod.storage.repository;
}