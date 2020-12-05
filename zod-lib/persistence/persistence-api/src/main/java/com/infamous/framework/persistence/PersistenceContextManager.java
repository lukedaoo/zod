package com.infamous.framework.persistence;

import javax.persistence.EntityManager;

public interface PersistenceContextManager {

    EntityManager getEntityManager();

    void close();
}
