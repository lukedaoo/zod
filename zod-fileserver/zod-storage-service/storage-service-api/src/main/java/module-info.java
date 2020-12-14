module zod.storage.api {
    requires static lombok;

    requires java.persistence;
    requires org.hibernate.orm.core;

    requires zod.persistence.api;
    requires zod.model.converter;

    exports com.infamous.zod.storage.converter;
    exports com.infamous.zod.storage.model;
    exports com.infamous.zod.storage.repository;

    opens com.infamous.zod.storage.converter;
    opens com.infamous.zod.storage.model;
    opens com.infamous.zod.storage.repository;
}