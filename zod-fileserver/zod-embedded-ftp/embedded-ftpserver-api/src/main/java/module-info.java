module embedded.ftpserver.api {
    requires static lombok;
    requires java.persistence;
    requires ftplet.api;
    requires ftpserver.core;

    requires system.property.utils;
    requires persistence.api;
    requires spring.beans;

    exports com.infamous.zod.ftp;
    exports com.infamous.zod.ftp.model;
    exports com.infamous.zod.ftp.um;

    opens com.infamous.zod.ftp;
    opens com.infamous.zod.ftp.model;
    opens com.infamous.zod.ftp.um;
}