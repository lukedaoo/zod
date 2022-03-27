package com.infamous.zod.songmgmt.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.infamous.zod.storage.repository.StorageFileDataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SongManagementDataStoreTest {
    private SongManagementDataStore m_ds;

    @BeforeEach
    private void setup() {
        m_ds = new SongManagementDataStore();
    }

    @Test
    public void testGetName() {
        assertEquals("SONG_MGMT_DS", m_ds.getDataStoreName());
    }
}