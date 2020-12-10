package com.infamous.framework.persistence;

import javax.persistence.EntityManager;

// Only for Testing
class TestingJPAEntityDataStoreManager extends EntityDataStore {

    private EntityManager m_manager;

    public TestingJPAEntityDataStoreManager(EMFactory factory) {
        m_manager = factory.createNewEntityManager();
    }

    public EntityManager getEntityManager() {
        return m_manager;
    }

    @Override
    public String getDataStoreName() {
        return "TESTING_DS";
    }

    public void beginTransaction() {
        m_manager.getTransaction().begin();
    }

    public void commitTransaction() {
        if (m_manager.getTransaction().isActive()) {
            m_manager.getTransaction().commit();
        }
    }

    public void rollbackTransaction() {
        if (m_manager.getTransaction().isActive()) {
            m_manager.getTransaction().rollback();
        }
    }

    public boolean isActive() {
        return m_manager.getTransaction().isActive();
    }

    public void close() {
        if (m_manager != null && m_manager.isOpen()) {
            m_manager.close();
        }
    }
}
