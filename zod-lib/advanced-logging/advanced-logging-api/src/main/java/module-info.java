import com.infamous.framework.logging.config.AdvancedLoggingConfiguration;

module advanced.logging.api {
    requires lombok;
    requires org.slf4j;

    requires sensitive.service.api;

    exports com.infamous.framework.logging.config;
    exports com.infamous.framework.logging.core;

    opens com.infamous.framework.logging.core;

    uses AdvancedLoggingConfiguration;
}