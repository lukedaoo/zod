module persistence.api {
    requires advanced.logging.app;
    requires lombok;
    requires java.transaction;
    requires java.persistence;

    exports com.infamous.framework.persistence.tx;
    exports com.infamous.framework.persistence;

    opens com.infamous.framework.persistence.tx;
    opens com.infamous.framework.persistence;
}