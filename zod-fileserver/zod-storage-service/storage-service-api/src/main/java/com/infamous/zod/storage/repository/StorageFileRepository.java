package com.infamous.zod.storage.repository;

import com.infamous.zod.base.rest.entity.UploadResult;
import com.infamous.zod.storage.model.StorageFileVO;
import java.io.File;
import java.util.Collection;
import java.util.List;

public interface StorageFileRepository {

    UploadResult<StorageFileVO> upload(Collection<StorageFileVO> files);

    boolean upload(StorageFileVO file);

    byte[] download(StorageFileVO file);

    StorageFileVO find(String id);

    File findPhysicalFile(String id);

    List<StorageFileVO> find(Collection<String> id);

    List<StorageFileVO> findAll();

    StorageFileVO findByChecksum(String checksum);
}
