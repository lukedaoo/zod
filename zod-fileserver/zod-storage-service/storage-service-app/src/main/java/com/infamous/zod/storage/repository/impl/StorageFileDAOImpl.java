package com.infamous.zod.storage.repository.impl;

import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.dao.AbstractDAO;
import com.infamous.framework.persistence.dao.EntityDAO;
import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileKey;
import com.infamous.zod.storage.repository.StorageFileDAO;
import com.infamous.zod.storage.repository.StorageFileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageFileDAOImpl extends AbstractDAO<StorageFile, StorageFileKey> implements
    EntityDAO<StorageFile, StorageFileKey>, StorageFileDAO {

    @Autowired
    public StorageFileDAOImpl(DataStoreManager dataStoreManager) {
        super(dataStoreManager, StorageFile.class, StorageFileDataStore.DS_NAME);
    }
}
