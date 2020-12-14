package com.infamous.zod.ftp.server;

import com.infamous.zod.ftp.um.config.AddAdminUserConfiguration;
import com.infamous.zod.ftp.um.config.FTPDataStoreConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({FTPDataStoreConfig.class, ZodFTPServerConfig.class, AddAdminUserConfiguration.class})
@Configuration
public @interface EnableZodFTPServer {

}
