package com.infamous.framework.persistence.dao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infamous.framework.persistence.DataStore;
import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.Person;
import java.util.Arrays;
import javax.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractDAOTest {

    private TestingAbstractDAO m_abstractDAO;
    private DataStore m_dataStore;

    @BeforeEach
    public void setup() {
        m_dataStore = mock(DataStore.class);
        DataStoreManager dsm = mock(DataStoreManager.class);

        when(dsm.getDatastore("TESTING_DS")).thenReturn(m_dataStore);

        m_abstractDAO = new TestingAbstractDAO(dsm);
    }

    @Test
    void merge() {
        m_abstractDAO.merge(new Person("1", "name"));

        verify(m_dataStore).merge(eq(new Person("1", "name")));
    }

    @Test
    void findById() {
        m_abstractDAO.findById("1");
        verify(m_dataStore).findById(eq(Person.class), eq("1"));
    }

    @Test
    void findByMultipleId() {
        m_abstractDAO.findById(Arrays.asList("1", "2", "3", "4"));
        verify(m_dataStore, times(4)).findById(eq(Person.class), anyString());
    }

    @Test
    void findByIdWithWriteLock() {
        m_abstractDAO.findByIdWithWriteLock("1");
        verify(m_dataStore).findById(eq(Person.class), eq("1"), eq(LockModeType.WRITE));
    }

    @Test
    void findByIdWithReadLock() {
        m_abstractDAO.findByIdWithReadLock("1");
        verify(m_dataStore).findById(eq(Person.class), eq("1"), eq(LockModeType.READ));
    }

    @Test
    void findAll() {
        m_abstractDAO.findAll();
        verify(m_dataStore).findAll(eq(Person.class));
    }

    @Test
    void persist() {
        m_abstractDAO.persist(new Person("1", "name"));
        verify(m_dataStore).persist(eq(new Person("1", "name")));
    }

    @Test
    void persistMultipleTimes() {
        m_abstractDAO.persist(Arrays.asList(
            new Person("1", "name1"),
            new Person("2", "name2"),
            new Person("3", "name3"),
            new Person("4", "name4")
        ));
        verify(m_dataStore, times(4)).persist(any(Person.class));
    }

    @Test
    void delete() {
        m_abstractDAO.delete(new Person("1", "name"));
        verify(m_dataStore).delete(eq(new Person("1", "name")));
    }

    @Test
    void deleteByID() {
        m_abstractDAO.deleteByID("1");
        verify(m_dataStore).deleteById(eq(Person.class), eq("1"));
    }

    @Test
    void deleteAll() {
        m_abstractDAO.deleteAll();
        verify(m_dataStore).deleteAll(eq(Person.class));
    }

    @Test
    void findByNativeQuery() {
        m_abstractDAO.findByNativeQuery("SOME SQL QUERIES");
        verify(m_dataStore).findByNativeQuery(eq("SOME SQL QUERIES"));
    }
}

