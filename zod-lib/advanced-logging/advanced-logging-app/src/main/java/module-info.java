module advanced.logging.app {
    requires static lombok;
    requires org.slf4j;

    requires advanced.logging.api;
    requires sensitive.service.api;
    requires sensitive.service.app;

    exports com.infamous.framework.logging;

    opens com.infamous.framework.logging.impl;
}