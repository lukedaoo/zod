module storage.service.api {
    requires static lombok;
    requires persistence.api;
    requires java.persistence;
    requires org.hibernate.orm.core;

    exports com.infamous.zod.storage.model;
    exports com.infamous.zod.storage.repository;

    opens com.infamous.zod.storage.model;
    opens com.infamous.zod.storage.repository;
}