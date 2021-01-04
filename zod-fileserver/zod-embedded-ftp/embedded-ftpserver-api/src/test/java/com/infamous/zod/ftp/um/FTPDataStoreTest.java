package com.infamous.zod.ftp.um;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FTPDataStoreTest {

    private FTPDataStore m_ds;

    @BeforeEach
    private void setup() {
        m_ds = new FTPDataStore();
    }

    @Test
    public void testGetName() {
        assertEquals("FTP_DS", m_ds.getDataStoreName());
    }
}