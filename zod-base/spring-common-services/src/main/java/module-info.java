module zod.spring.common {
    requires spring.context;

    requires zod.logging.app;
    requires zod.file.service;

    exports com.infamous.zod.base.common;
    exports com.infamous.zod.base.common.service;
}