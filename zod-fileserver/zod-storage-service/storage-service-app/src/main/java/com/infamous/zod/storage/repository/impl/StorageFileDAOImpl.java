package com.infamous.zod.storage.repository.impl;

import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.dao.AbstractDAO;
import com.infamous.framework.persistence.dao.EntityDAO;
import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileKey;
import com.infamous.zod.storage.repository.StorageFileDAO;
import com.infamous.zod.storage.repository.StorageFileDataStore;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("unchecked")
@Component
public class StorageFileDAOImpl extends AbstractDAO<StorageFile, StorageFileKey> implements
    EntityDAO<StorageFile, StorageFileKey>, StorageFileDAO {

    @Autowired
    public StorageFileDAOImpl(DataStoreManager dataStoreManager) {
        super(dataStoreManager, StorageFile.class, StorageFileDataStore.DS_NAME);
    }

    @Override
    public List<StorageFile> findById(List<StorageFileKey> storageFileKeys) {
        TypedQuery<Object[]> q = (TypedQuery<Object[]>) createNativeQuery(
            "SELECT id, fileName, enabled FROM StorageFile WHERE id IN (:ids)");
        q.setParameter("ids", storageFileKeys.stream().map(StorageFileKey::getId).collect(Collectors.toList()));
        List<Object[]> list = q.getResultList();

        return list.stream()
            .map(this::buildStoreFile)
            .collect(Collectors.toList());
    }

    @Override
    public List<StorageFile> findAll() {
        List<Object[]> objects = (List<Object[]>) findByNativeQuery(
            "SELECT id, fileName, enabled FROM StorageFile WHERE enabled = 1");
        return objects.stream()
            .map(this::buildStoreFile)
            .collect(Collectors.toList());
    }

    @Override
    public StorageFile findByChecksum(String checksum) {
        TypedQuery<Object[]> q = (TypedQuery<Object[]>) createNativeQuery(
            "SELECT DISTINCT id, fileName, enabled, checksum FROM StorageFile WHERE checksum = (:checksum)");
        q.setParameter("checksum", checksum);

        Object[] objects = q.getSingleResult();
        return buildStoreFile(objects);
    }

    @Override
    public StorageFile findByFileName(String fileName) {
        TypedQuery<Object[]> q = (TypedQuery<Object[]>) createNativeQuery(
            "SELECT DISTINCT id, fileName, enabled FROM StorageFile WHERE fileName = (:fileName)");
        q.setParameter("fileName", fileName);

        Object[] objects = q.getSingleResult();
        return buildStoreFile(objects);
    }


    private StorageFile buildStoreFile(Object[] objArr) {
        StorageFile res = StorageFile.builder()
            .id((String) objArr[0])
            .fileName((String) objArr[1])
            .enabled((Boolean) objArr[2])
            .build();
        if (objArr.length > 3) {
            res.setChecksum((String) objArr[3]);
        }
        return res;
    }
}
