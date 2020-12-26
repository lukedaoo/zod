package com.infamous.framework.persistence.utils;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

public class JPAEntityManagerFactory {

    private EntityManagerFactory m_factory;

    public JPAEntityManagerFactory() {

    }

    public JPAEntityManagerFactory(String persistenceUnitName, Map<String, String> properties) {
        initEntityMgr(persistenceUnitName, properties);
    }

    public JPAEntityManagerFactory(String persistenceUnitName) {
        initEntityMgr(persistenceUnitName, null);
    }

    private void initEntityMgr(String persistenceUnitName, Map<String, String> properties) {
        HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
        m_factory = persistenceProvider.createEntityManagerFactory(persistenceUnitName, properties);
    }

    public EntityManager createNewEntityManager() {
        return m_factory.createEntityManager();
    }
}
