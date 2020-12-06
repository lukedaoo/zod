package com.infamous.framework.persistence;

import javax.persistence.EntityManager;

public interface EMFactory {

    EntityManager createNewEntityManager();

}
