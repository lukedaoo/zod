package com.infamous.zod.storage.repository.impl;

import com.infamous.framework.file.FileService;
import com.infamous.framework.file.FileStorageException;
import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.storage.converter.StorageFileConverter;
import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileKey;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileDAO;
import com.infamous.zod.storage.repository.StorageFileRepository;
import com.infamous.zod.storage.repository.UploadResult;
import java.io.File;
import java.util.Collection;
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
public class StorageFileRepositoryImpl implements StorageFileRepository {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(StorageFileRepositoryImpl.class, "storage.service");

    private StorageFileDAO m_dao;
    private StorageFileConverter m_converter;
    private FileService m_fileService;

    public StorageFileRepositoryImpl() {
    }

    @Autowired
    public StorageFileRepositoryImpl(StorageFileDAO dao, StorageFileConverter converter, FileService coreFileService) {
        m_dao = dao;
        m_converter = converter;
        m_fileService = coreFileService;
    }

    @Override
    public boolean upload(StorageFileVO file) {

        long fileSize = saveToDisk(file).orElseThrow(() -> new FileStorageException("File size isn't present"));
        file.setFileSize(fileSize);

        try {
            String dbId = saveToDB(file).orElseThrow(() -> new FileStorageException("DB Id isn't present"));
            file.setId(dbId);
        } catch (Exception e) {
            LOGGER.warn("Exception occurred while saving to DB. Rollback transaction and delete file..", e);
            m_fileService.delete(file.getFileName());
            return false;
        }
        return true;
    }

    private Optional<String> saveToDB(StorageFileVO file) {
        StorageFile sf = m_converter.toEntity(file);
        boolean res = m_dao.persist(sf);

        return res ? Optional.of(sf.getId()) : Optional.empty();
    }

    private Optional<Long> saveToDisk(StorageFileVO file) {
        long size = m_fileService.store(file.getContent(), file.getFileName());
        return size >= 0 ? Optional.of(size) : Optional.empty();
    }


    @Override
    public UploadResult upload(Collection<StorageFileVO> files) {
        UploadResult res = new UploadResult();
        List<StorageFileVO> data = new LinkedList<>();

        files.forEach(f -> {
            try {
                if (upload(f)) {
                    data.add(f);
                }
            } catch (Exception e) {
                LOGGER.error("Error while uploading file [" + f.getFileName() + "]", e);
            }
        });
        res.setData(data);
        if (data.size() == files.size()) {
            res.setStatus("success");
        } else {
            res.setStatus("failure");
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
    public List<StorageFileVO> find(Collection<String> id) {
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
