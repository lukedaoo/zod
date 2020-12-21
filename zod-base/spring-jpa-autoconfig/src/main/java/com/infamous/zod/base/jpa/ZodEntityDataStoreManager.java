package com.infamous.zod.base.jpa;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.framework.persistence.DataStore;
import com.infamous.framework.persistence.DataStoreManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ZodEntityDataStoreManager implements DataStoreManager {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(ZodEntityDataStoreManager.class, "spring.jpa");

    private Map<String, DataStore> m_datastoreMap = new HashMap<>();

    public ZodEntityDataStoreManager() {
    }

    @Override
    public DataStore getDatastore(String name) {
        return m_datastoreMap.get(name);
    }

    @Override
    public void register(String name, DataStore dataStore) {
        LOGGER.info("Register Datastore [" + name + "]");
        m_datastoreMap.put(name, dataStore);
    }

    @Override
    public void unregister(String name) {
        LOGGER.info("Unregister Datastore [" + name + "]");
        m_datastoreMap.remove(name);
    }

    @Override
    public void close(String name) {
        Optional.ofNullable(m_datastoreMap.get(name))
            .ifPresent(ds -> ds.getEntityManager().close());
    }

    @Override
    public void destroy() {
        m_datastoreMap.keySet().forEach(this::close);
        m_datastoreMap.clear();
        LOGGER.info("Destroy DatastoreManager");
    }
}
