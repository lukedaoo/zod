package com.infamous.framework.persistence;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import java.util.List;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class AbstractDataStore implements DataStore {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(AbstractDataStore.class, "persistence.app");

    public AbstractDataStore() {

    }

    @Override
    public void close() {
        getEntityManager().close();
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    @Override
    public boolean isOpen() {
        return getEntityManager().isOpen();
    }

    @Override
    public <E> boolean create(E entity) {
        persist(entity);
        return true;
    }

    @Override
    public <E> boolean merge(E entity) {
        getEntityManager().merge(entity);
        return true;
    }

    /**
    // Not use for removing a detached instance
    // Use instead {@link #deleteById(Class, Object)}}
     **/
    @Override
    public <E> boolean delete(E entity) {
        LOGGER.trace("Delete entity called for : " + entity);
        getEntityManager().remove(entity);
        LOGGER.trace("Delete entity returned for : " + entity);
        return true;
    }

    @Override
    public <E> int deleteAll(Class<E> clazz) {
        LOGGER.trace("deleteAll called for : " + clazz);

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<E> cd = cb.createCriteriaDelete(clazz);

        cd.from(clazz);

        Query query = getEntityManager().createQuery(cd);
        int removedNum = query.executeUpdate();

        LOGGER.trace("deleteAll returned for : " + clazz + "Result: " + removedNum);
        return removedNum;
    }

    @Override
    public <E> void lock(E entity, LockModeType lockMode) {
        getEntityManager().lock(entity, lockMode);
    }

    @Override
    public <E> void lock(E entity) {
        lock(entity, LockModeType.PESSIMISTIC_WRITE);
    }

    @Override
    public <E> void persist(E entity) {
        LOGGER.trace("Persist entity called for : {}", entity);
        getEntityManager().persist(entity);
        LOGGER.trace("Persist entity returned for : {}", entity);
    }

    @Override
    public List findByNativeQuery(String query) {
        return getEntityManager().createNativeQuery(query)
            .getResultList();
    }

    @Override
    public <E> E findById(Class<E> clazz, Object primaryKey) {
        return findById(clazz, primaryKey, LockModeType.PESSIMISTIC_READ);
    }

    @Override
    public <E> E findById(Class<E> clazz, Object primaryKey, LockModeType lockMode) {
        LOGGER.trace("findById called for [" + clazz + "] "
            + "with primary key [" + primaryKey + "] and lock mode [" + lockMode
            + "]");

        E result = getEntityManager().find(clazz, primaryKey, lockMode);

        LOGGER.trace("findById entity returned " + result);
        return result;
    }

    @Override
    public <E> List<E> findAll(Class<E> clazz) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<E> rootEntry = criteriaQuery.from(clazz);

        CriteriaQuery<E> all = criteriaQuery.select(rootEntry);

        TypedQuery<E> allQuery = getEntityManager().createQuery(all);
        return logAndReturnQueryResult("findAll", clazz, allQuery);
    }

    private <E> List<E> logAndReturnQueryResult(String methodName, Class<E> clazz, TypedQuery<E> allQuery) {
        LOGGER.trace(methodName + " called for class: " + clazz + " and query: " + allQuery);

        LockModeType lockModeType = allQuery.getLockMode();
        if (lockModeType == null) {
            allQuery.setLockMode(LockModeType.PESSIMISTIC_READ);
        }

        List<E> resultList = allQuery.getResultList();
        LOGGER.trace(methodName + " returned for class: " + clazz + " and query: " + allQuery);
        return resultList;
    }

    @Override
    public <E> boolean deleteById(Class<E> clazz, Object id) {
        E entity = findById(clazz,id);
        return delete(entity);
    }
}
