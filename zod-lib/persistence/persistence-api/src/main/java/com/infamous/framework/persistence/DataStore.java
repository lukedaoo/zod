package com.infamous.framework.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public interface DataStore {

    EntityManager getEntityManager();

    String getDataStoreName();

    void close();

    void flush();

    boolean isOpen();

    <E> boolean merge(E entity);

    <E> boolean delete(E entity);

    <E> boolean deleteById(Class<E> clazz, Object id);

    <E> int deleteAll(Class<E> clazz);

    <E> void lock(E entity, LockModeType lockMode);

    <E> void lock(E entity);

    <E> boolean persist(E entity);

    <E> List<E> findByNativeQuery(Class<E> clazz, String query);

    List findByNativeQuery(String query);

    <E> E findById(Class<E> clazz, Object primaryKey);

    <E> E findById(Class<E> clazz, Object primaryKey, LockModeType lockMode);

    <E> List<E> findAll(Class<E> clazz);
}
