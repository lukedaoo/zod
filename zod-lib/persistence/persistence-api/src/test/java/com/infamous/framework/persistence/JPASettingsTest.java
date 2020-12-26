package com.infamous.framework.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class JPASettingsTest {


    @Test
    public void testInitObject_Exception() {
        assertThrows(IllegalArgumentException.class, () -> JPASettings.builder().url(null).build());
        assertThrows(IllegalArgumentException.class, () -> JPASettings.builder().user(null).build());
        assertThrows(IllegalArgumentException.class, () -> JPASettings.builder().password(null).build());
        assertThrows(IllegalArgumentException.class, () -> JPASettings.builder().datasourceClassName(null).build());
    }

    @Test
    public void testInitObject() {
        JPASettings settings = JPASettings.builder()
            .url("url")
            .user("sa")
            .password("sa")
            .datasourceClassName("com.mysql.BaseDataSource")
            .build();

        settings.put("maxIdleTime", 1000);

        assertEquals("url", settings.getDatabaseUrl());
        assertEquals("sa", settings.getUser());
        assertEquals("sa", settings.getPassword());
        assertEquals("com.mysql.BaseDataSource", settings.getDatasourceClassName());
        assertEquals(1000, Integer.valueOf(settings.get("maxIdleTime").toString()));
    }
}