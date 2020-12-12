module spring.common.services {
    requires spring.context;

    requires advanced.logging.app;
    requires file.service;

    exports com.infamous.zod.base.common;
    exports com.infamous.zod.base.common.service;
}