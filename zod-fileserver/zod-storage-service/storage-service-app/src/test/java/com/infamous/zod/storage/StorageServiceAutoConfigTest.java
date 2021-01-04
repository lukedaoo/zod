package com.infamous.zod.storage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.infamous.framework.persistence.DataStoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageServiceAutoConfigTest {

    private StorageServiceAutoConfig m_config;

    @BeforeEach
    public void setup() {
        m_config = new StorageServiceAutoConfig();
    }

    @Test
    public void testCreateEmf() {
        assertNotNull(m_config.createStorageFileEmf());
    }

    @Test
    public void testCreateDS() {
        assertNotNull(m_config.createStorageFileDataStore(mock(DataStoreManager.class)));
    }

    @Test
    public void testCreateConverter() {
        assertNotNull(m_config.createConverter());
    }
}