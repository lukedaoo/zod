package com.infamous.framework.persistence.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;

public interface EntityDAO<E, PK extends Serializable> {

    boolean merge(E entity);

    E findById(PK primaryKey);

    List<E> findById(List<PK> pks);

    E findByIdWithWriteLock(PK primaryKey);

    E findByIdWithReadLock(PK primaryKey);

    List<E> findAll();

    boolean persist(E entity);

    void persist(List<E> entities);

    boolean delete(E entity);

    boolean deleteByID(PK primaryKey);

    int deleteAll();

    List findByNativeQuery(String query);

    Query createNativeQuery(String query);
}
