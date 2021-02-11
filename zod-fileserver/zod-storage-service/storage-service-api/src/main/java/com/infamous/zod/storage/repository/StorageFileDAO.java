package com.infamous.zod.storage.repository;

import com.infamous.framework.persistence.dao.EntityDAO;
import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileKey;

public interface StorageFileDAO extends EntityDAO<StorageFile, StorageFileKey> {

    StorageFile findByChecksum(final String checksum);

    StorageFile findByFileName(final String fileName);

    default boolean doesExistByChecksum(final String checksum) {
        return findByChecksum(checksum) != null;
    }

    default boolean doesExistByFileName(final String fileName) {
        return findByFileName(fileName) != null;
    }
}
