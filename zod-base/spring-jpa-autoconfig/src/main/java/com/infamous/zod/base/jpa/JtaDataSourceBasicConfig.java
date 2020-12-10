package com.infamous.zod.base.jpa;

import com.infamous.framework.common.SystemPropertyUtils;
import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.JPASettings;
import org.springframework.context.annotation.Bean;

public class JtaDataSourceBasicConfig {

    @Bean
    public JPASettings dataSourceConfig() {
        return JPASettings.builder()
            .url(getProp("db.url"))
            .user(getProp("db.username"))
            .password(getProp("db.password"))
            .datasourceClassName(getProp("db.dataSourceClassName"))
            .build();
    }

    @Bean(destroyMethod = "destroy")
    public DataStoreManager dataStoreManager() {
        return new ZodEntityDataStoreManager();
    }

    private String getProp(String prop) {
        return SystemPropertyUtils.getInstance().getProperty(prop, null);
    }
}
