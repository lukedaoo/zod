package com.infamous.zod.base.jpa;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.framework.persistence.DataStore;
import com.infamous.framework.persistence.DataStoreManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ZodEntityDataStoreManager implements DataStoreManager {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(ZodEntityDataStoreManager.class, "spring.jpa");

    private Map<String, DataStore> m_datastoreMap = new HashMap<>();

    public ZodEntityDataStoreManager() {
    }

    @Override
    public DataStore getDatastore(String name) {
        return Optional.ofNullable(m_datastoreMap.get(name))
            .orElseThrow(() -> new IllegalArgumentException("Datastore [" + name + "] was not registered"));
    }

    @Override
    public void register(String name, DataStore dataStore) {
        Objects.requireNonNull(dataStore);
        m_datastoreMap.put(name, dataStore);
        LOGGER.info("Registered Datastore [" + name + "]");
    }

    @Override
    public void unregister(String name) {
        close(name);
        m_datastoreMap.remove(name);
        LOGGER.info("Unregistered Datastore [" + name + "]");
    }

    @Override
    public void close(String name) {
        getDatastore(name).getEntityManager().close();
        LOGGER.info("Datastore [" + name + "] is closed");
    }

    @Override
    public void destroy() {
        m_datastoreMap.keySet().forEach(name -> {
            try {
                close(name);
            } catch (Exception e) {
                LOGGER.error("Error while closing Datastore [" + name + "]", e);
            }
        });
        m_datastoreMap.clear();
    }
}
