module zod.logging.app {
    requires static lombok;
    requires org.slf4j;

    requires zod.logging.api;
    requires zod.sensitive.api;
    requires zod.sensitive.app;

    exports com.infamous.framework.logging;

    opens com.infamous.framework.logging.impl;
}