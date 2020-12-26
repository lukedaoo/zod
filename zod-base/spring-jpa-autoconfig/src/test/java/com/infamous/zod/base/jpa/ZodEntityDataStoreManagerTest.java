package com.infamous.zod.base.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infamous.framework.persistence.DataStore;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZodEntityDataStoreManagerTest {

    private ZodEntityDataStoreManager m_dataStoreManager;
    private DataStore m_dataStore;

    @BeforeEach
    public void test() {
        m_dataStoreManager = new ZodEntityDataStoreManager();
        m_dataStore = mock(DataStore.class);
        EntityManager entityManager = mock(EntityManager.class);
        when(m_dataStore.getEntityManager()).thenReturn(entityManager);
        doNothing().when(entityManager).close();
    }

    @Test
    public void testGetDataStore_Null() {
        assertThrows(IllegalArgumentException.class, () -> m_dataStoreManager.getDatastore("NOT_EXIST"));
    }

    @Test
    public void testRegisterDatastore() {
        m_dataStoreManager.register("ENTITY_DS", m_dataStore);

        assertNotNull(m_dataStoreManager.getDatastore("ENTITY_DS"));
    }

    @Test
    public void testRegisterDatastore_WithNullDS() {
        assertThrows(NullPointerException.class, () -> m_dataStoreManager.register("DS", null));
    }

    @Test
    public void testUnRegisterDataStore() {
        m_dataStoreManager.register("ENTITY_DS", m_dataStore);

        assertNotNull(m_dataStoreManager.getDatastore("ENTITY_DS"));

        m_dataStoreManager.unregister("ENTITY_DS");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> m_dataStoreManager.getDatastore("ENTITY_DS"));

        assertEquals("Datastore [ENTITY_DS] was not registered", exception.getMessage());
    }


    @Test
    public void testUnRegisterDataStore_ThatWasNotRegistered() {
        m_dataStoreManager.register("ENTITY_DS", m_dataStore);

        assertNotNull(m_dataStoreManager.getDatastore("ENTITY_DS"));

        assertThrows(IllegalArgumentException.class, () -> m_dataStoreManager.unregister("ENTITY"));
    }


    @Test
    public void testClose() {
        m_dataStoreManager.register("ENTITY_DS", m_dataStore);
        m_dataStoreManager.close("ENTITY_DS");

        verify(m_dataStore).getEntityManager();
        verify(m_dataStore.getEntityManager()).close();
    }

    @Test
    public void testDestroy() {
        m_dataStoreManager.register("ENTITY_DS", m_dataStore);
        assertNotNull(m_dataStoreManager.getDatastore("ENTITY_DS"));

        m_dataStoreManager.destroy();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> m_dataStoreManager.getDatastore("ENTITY_DS"));
        assertEquals("Datastore [ENTITY_DS] was not registered", exception.getMessage());
    }

    @Test
    public void testDestroy_2() {
        DataStore ds1 = mock(DataStore.class);
        EntityManager em1 = mock(EntityManager.class);
        when(ds1.getEntityManager()).thenReturn(em1);

        DataStore ds2 = mock(DataStore.class);
        EntityManager em2 = mock(EntityManager.class);
        when(ds2.getEntityManager()).thenReturn(em2);
        doThrow(RuntimeException.class).when(em2).close();

        m_dataStoreManager.register("ENTITY_DS_1", ds1);
        m_dataStoreManager.register("ENTITY_DS_2", ds2);
        assertNotNull(m_dataStoreManager.getDatastore("ENTITY_DS_1"));
        assertNotNull(m_dataStoreManager.getDatastore("ENTITY_DS_2"));

        m_dataStoreManager.destroy();

        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
            () -> m_dataStoreManager.getDatastore("ENTITY_DS_1"));
        assertEquals("Datastore [ENTITY_DS_1] was not registered", e1.getMessage());


        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
            () -> m_dataStoreManager.getDatastore("ENTITY_DS_2"));
        assertEquals("Datastore [ENTITY_DS_2] was not registered", e2.getMessage());
    }
}