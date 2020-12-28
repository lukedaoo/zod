package com.infamous.zod.base.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.infamous.framework.persistence.JPASettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JtaDataSourceBasicConfigTest {

    private JtaDataSourceBasicConfig m_config;

    @BeforeEach
    public void setup() {
        m_config = new JtaDataSourceBasicConfig();
        System.setProperty("db.url", "DB_URL");
        System.setProperty("db.username", "sa");
        System.setProperty("db.password", "sa");
        System.setProperty("db.dataSourceClassName", "com.infamous.jdbc.BasicDataSource");
    }

    @AfterEach
    public void tearDown() {
        System.clearProperty("db.url");
        System.clearProperty("db.username");
        System.clearProperty("db.password");
        System.clearProperty("db.dataSourceClassName");
    }

    @Test
    public void testJPAConfig() {
        JPASettings settings = m_config.createDataSourceConfig();
        assertNotNull(settings);
        assertEquals("DB_URL", settings.getDatabaseUrl());
        assertEquals("sa", settings.getUser());
        assertEquals("sa", settings.getPassword());
        assertEquals("com.infamous.jdbc.BasicDataSource", settings.getDatasourceClassName());
    }
}