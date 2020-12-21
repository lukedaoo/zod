package com.infamous.zod.storage.repository.impl;

import com.infamous.framework.file.FileService;
import com.infamous.framework.file.FileStorageException;
import com.infamous.zod.storage.converter.StorageFileConverter;
import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileKey;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileDAO;
import com.infamous.zod.storage.repository.StorageFileRepository;
import com.infamous.zod.storage.repository.UploadResult;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class StorageRepositoryImpl implements StorageFileRepository {

    private StorageFileDAO m_dao;
    private StorageFileConverter m_converter;
    private FileService m_fileService;

    public StorageRepositoryImpl() {
    }

    @Autowired
    public StorageRepositoryImpl(StorageFileDAO dao, StorageFileConverter converter, FileService coreFileService) {
        m_dao = dao;
        m_converter = converter;
        m_fileService = coreFileService;
    }

    @Override
    public boolean upload(StorageFileVO file) {
        return saveToDisk(file) && saveToDB(file);
    }

    private boolean saveToDB(StorageFileVO file) {
        StorageFile sf = m_converter.toEntity(file);
        boolean res = m_dao.persist(sf);
        file.setId(sf.getId());
        return res;
    }

    private boolean saveToDisk(StorageFileVO file) {
        long size = m_fileService.store(file.getContent(), file.getFileName());
        file.setFileSize(size);
        return size >= 0;
    }


    @Override
    public UploadResult upload(List<StorageFileVO> files) {
        UploadResult res = new UploadResult();
        List<StorageFileVO> data = new LinkedList<>();

        files.forEach(f -> {
            if (upload(f)) {
                data.add(f);
            }
        });
        res.setData(data);
        if (data.size() == files.size()) {
            res.setStatus("success");
        }
        return res;
    }

    @Override
    public byte[] download(StorageFileVO file) {
        return m_fileService.getFileAsByteArray(file.getFileName());
    }

    @Override
    public StorageFileVO find(String id) {
        StorageFile sf = m_dao.findById(new StorageFileKey(id));
        return sf != null ? m_converter.toDTO(sf) : null;
    }

    @Override
    public File findPhysicalFile(String id) {
        StorageFileVO file = find(id);
        return Optional.ofNullable(file)
            .map(f -> m_fileService.getFilePhysical(f.getFileName()))
            .orElseThrow(() -> new FileStorageException("File with [" + id + "] does not exist"));
    }

    @Override
    public List<StorageFileVO> find(List<String> id) {
        List<StorageFile> sfs = m_dao
            .findById(id.stream().map(StorageFileKey::new).collect(Collectors.toList()));
        return m_converter.toDTO(sfs.parallelStream());
    }

    @Override
    public List<StorageFileVO> findAll() {
        return Optional.ofNullable(m_dao.findAll())
            .map(entities -> m_converter.toDTO(entities.parallelStream()))
            .orElse(Collections.emptyList());
    }
}
