package com.infamous.zod.storage.repository;

import com.infamous.framework.persistence.EntityDataStore;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class StorageFileDataStore extends EntityDataStore {

    public static final String DS_NAME = "STORAGE_FILE_DS";

    @PersistenceContext(unitName = "fileserver-ds")
    private EntityManager m_entityManager;

    public StorageFileDataStore() {

    }

    @Override
    public EntityManager getEntityManager() {
        return m_entityManager;
    }

    @Override
    public String getDataStoreName() {
        return DS_NAME;
    }
}
