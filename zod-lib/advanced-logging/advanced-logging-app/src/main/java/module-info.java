module advanced.logging.app {
    requires org.slf4j;
    requires lombok;

    requires advanced.logging.api;
    requires sensitive.service.api;
    requires sensitive.service.app;

    exports com.infamous.framework.logging;

    opens com.infamous.framework.logging.impl;
}