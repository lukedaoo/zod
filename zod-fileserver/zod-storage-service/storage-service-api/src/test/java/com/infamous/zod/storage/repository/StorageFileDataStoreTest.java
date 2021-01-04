package com.infamous.zod.storage.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageFileDataStoreTest {

    private StorageFileDataStore m_ds;

    @BeforeEach
    private void setup() {
        m_ds = new StorageFileDataStore();
    }

    @Test
    public void testGetName() {
        assertEquals("STORAGE_FILE_DS", m_ds.getDataStoreName());
    }
}
