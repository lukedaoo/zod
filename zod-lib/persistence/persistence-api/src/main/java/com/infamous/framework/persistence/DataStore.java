package com.infamous.framework.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public interface DataStore {

    EntityManager getEntityManager();

    void close();

    void flush();

    boolean isOpen();

    <E> boolean create(E entity);

    <E> boolean merge(E entity);

    <E> boolean delete(E entity);

    <E> boolean deleteById(Class<E> clazz, Object id);

    <E> int deleteAll(Class<E> clazz);

    <E> void lock(E entity, LockModeType lockMode);

    <E> void lock(E entity);

    <E> void persist(E entity);

    List findByNativeQuery(String query);

    <E> E findById(Class<E> clazz, Object primaryKey);

    <E> E findById(Class<E> clazz, Object primaryKey, LockModeType lockMode);

    <E> List<E> findAll(Class<E> clazz);
}
