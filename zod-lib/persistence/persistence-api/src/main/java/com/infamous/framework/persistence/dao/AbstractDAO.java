package com.infamous.framework.persistence.dao;

import com.infamous.framework.persistence.DataStore;
import com.infamous.framework.persistence.DataStoreManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.LockModeType;

public abstract class AbstractDAO<T, PK extends Serializable> implements EntityDAO<T, PK> {

    private DataStoreManager m_dataStoreManager;
    private Class<T> m_clazz;
    private String m_dsName;

    public AbstractDAO(DataStoreManager dataStoreManager, Class<T> type, String dsName) {
        m_dataStoreManager = dataStoreManager;
        m_clazz = type;
        m_dsName = dsName;
    }

    public String getDataStoreName() {
        return m_dsName;
    }

    private DataStore getDataStore() {
        return Optional.ofNullable(m_dataStoreManager.getDatastore(m_dsName))
            .orElseThrow(
                () -> new IllegalStateException("DataStore for name [" + m_dsName + "] doesn't register"));

    }

    @Override
    public boolean merge(T entity) {
        return getDataStore().merge(entity);
    }

    @Override
    public T findById(PK primaryKey) {
        return getDataStore().findById(m_clazz, primaryKey);
    }

    @Override
    public T findByIdWithWriteLock(PK primaryKey) {
        return getDataStore().findById(m_clazz, primaryKey, LockModeType.WRITE);
    }

    @Override
    public T findByIdWithReadLock(PK primaryKey) {
        return getDataStore().findById(m_clazz, primaryKey, LockModeType.READ);
    }

    @Override
    public List<T> findAll() {
        return getDataStore().findAll(m_clazz);
    }

    @Override
    public boolean persist(T entity) {
        return getDataStore().persist(entity);
    }

    @Override
    public void persist(List<T> entities) {
        entities.forEach(this::persist);
    }

    @Override
    public boolean delete(T entity) {
        return getDataStore().delete(entity);
    }

    @Override
    public boolean deleteByID(PK primaryKey) {
        return getDataStore().deleteById(m_clazz, primaryKey);
    }

    @Override
    public int deleteAll() {
        return getDataStore().deleteAll(m_clazz);
    }

    @Override
    public List findByNativeQuery(String query) {
        return getDataStore().findByNativeQuery(query);
    }

    @Override
    public List<T> findById(List<PK> pks) {
        return pks.stream().map(pk -> findById(pk))
            .collect(Collectors.toList());
    }
}
