import com.infamous.framework.logging.config.AdvancedLoggingConfiguration;

module zod.logging.api {
    requires static lombok;
    requires org.slf4j;

    requires zod.sensitive.api;

    exports com.infamous.framework.logging.config;
    exports com.infamous.framework.logging.core;

    opens com.infamous.framework.logging.core;

    uses AdvancedLoggingConfiguration;
}