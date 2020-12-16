package com.infamous.zod.storage.repository;

import com.infamous.zod.storage.model.StorageFileVO;
import java.util.List;

public interface StorageFileRepository {
    UploadResult upload(List<StorageFileVO> files);

    boolean upload(StorageFileVO file);

    byte[] download(StorageFileVO file);

    StorageFileVO find(String id);

    List<StorageFileVO> find(List<String> id);

    List<StorageFileVO> findAll();
}
