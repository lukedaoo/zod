module zod.sensitive.app {
    requires zod.sensitive.api;
    exports com.infamous.framework.sensitive.service;
    opens com.infamous.framework.sensitive.service;
}