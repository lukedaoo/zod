package com.infamous.zod.ftp.um;

import com.infamous.framework.persistence.EntityDataStore;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class FTPDataStore extends EntityDataStore {

    public static final String DS_NAME = "FTP_DS";

    @PersistenceContext(unitName = "ftp-ds")
    private EntityManager m_entityManager;

    public FTPDataStore() {
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
