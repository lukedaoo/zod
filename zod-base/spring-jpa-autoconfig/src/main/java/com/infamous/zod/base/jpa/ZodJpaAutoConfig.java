package com.infamous.zod.base.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({JtaTransactionManagementConfig.class, JtaDataSourceBasicConfig.class})
@Configuration
public @interface ZodJpaAutoConfig {

}
