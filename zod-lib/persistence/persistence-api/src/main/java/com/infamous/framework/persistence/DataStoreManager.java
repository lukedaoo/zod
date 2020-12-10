package com.infamous.framework.persistence;

public interface DataStoreManager {

    DataStore getDatastore(String name);

    void register(String name, DataStore dataStore);

    void unregister(String name);

    void close(String name);

    void destroy();
}
