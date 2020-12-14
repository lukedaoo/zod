package com.infamous.zod.storage.repository.impl;

import com.infamous.framework.file.FileService;
import com.infamous.zod.storage.converter.StorageFileConverter;
import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileDAO;
import com.infamous.zod.storage.repository.StorageFileRepository;
import com.infamous.zod.storage.repository.UploadResult;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

public class StorageRepositoryImpl implements StorageFileRepository {

    private StorageFileDAO m_dao;
    private StorageFileConverter m_converter;
    private FileService m_fileService;

    @Autowired
    public StorageRepositoryImpl(StorageFileDAO dao, StorageFileConverter converter, FileService coreFileService) {
        m_dao = dao;
        m_converter = converter;
        m_fileService = coreFileService;
    }

    @Transactional
    @Override
    public boolean upload(StorageFileVO file) {
        return saveToDisk(file) && saveToDB(file);
    }

    private boolean saveToDB(StorageFileVO file) {
        StorageFile sf = m_converter.toEntity(file);
        boolean res = m_dao.persist(sf);
        file.setId(sf.getId());
        file.setDownloadUrl("/download/" + file.getId());
        return res;
    }

    private boolean saveToDisk(StorageFileVO file) {
        long size = m_fileService.store(file.getContent(), file.getFileName());
        file.setFileSize(size);
        return size >= 0;
    }


    @Override
    public UploadResult upload(List<StorageFileVO> files) {
        return null;
    }

    @Override
    public byte[] download(StorageFileVO file) {
        return new byte[0];
    }

    @Override
    public StorageFileVO find(String id) {
        return null;
    }

    @Override
    public List<StorageFileVO> find(List<String> id) {
        return null;
    }

    @Override
    public List<StorageFileVO> findAll() {
        return null;
    }

    @Override
    public void reset() {

    }
}
