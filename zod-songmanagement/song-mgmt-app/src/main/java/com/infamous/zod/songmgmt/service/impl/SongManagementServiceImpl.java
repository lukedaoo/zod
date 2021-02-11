package com.infamous.zod.songmgmt.service.impl;

import com.infamous.framework.http.core.BodyPart;
import com.infamous.zod.base.rest.entity.UploadResult;
import com.infamous.zod.songmgmt.model.SongVO;
import com.infamous.zod.songmgmt.repository.SongRepository;
import com.infamous.zod.songmgmt.service.SongManagementService;
import com.infamous.zod.storage.StorageFileRestClient;
import com.infamous.zod.storage.model.StorageFileVO;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongManagementServiceImpl implements SongManagementService {

    private SongRepository m_songRepository;
    private StorageFileRestClient m_storageFileRestClient;

    @Autowired
    public SongManagementServiceImpl(SongRepository songRepository, StorageFileRestClient storageFileRestClient) {
        this.m_songRepository = songRepository;
        this.m_storageFileRestClient = storageFileRestClient;
    }

    @Override
    public SongVO upload(SongVO songVO, StorageFileVO file) {
        return m_storageFileRestClient.upload(UploadFilePart.create("file", file))
            .thenApply(f -> {
                songVO.setFileId(f.getId());
                m_songRepository.upload(songVO);
                return songVO;
            })
            .join();
    }

    @Override
    public UploadResult<SongVO> multipleUpload(List<SongVO> songs, List<StorageFileVO> files) {
        List<UploadFilePart> filePartsToUpload = buildUploadFileParts("files", files);
        return m_storageFileRestClient.upload(filePartsToUpload)
            .thenApply(storageFileVOUploadResult -> {
                Map<String, String> fileNameToId = storageFileVOUploadResult.getData().stream()
                    .collect(Collectors.toMap(StorageFileVO::getFileName, StorageFileVO::getId));
                songs.forEach(s -> s.setFileId(fileNameToId.get(s.getFileName())));

                return m_songRepository.upload(songs);
            }).join();
    }

    @Override
    public List<SongVO> findAll() {
       return m_songRepository.list();
    }

    private List<UploadFilePart> buildUploadFileParts(String name, List<StorageFileVO> files) {
        return files.stream()
            .map(file -> UploadFilePart.create(name, file))
            .collect(Collectors.toList());
    }

    private static class UploadFilePart extends BodyPart<InputStream> {

        private final String m_fileName;

        private UploadFilePart(String name, StorageFileVO fileVO) {
            super(name, fileVO.getContent(), null);
            m_fileName = fileVO.getFileName();
        }

        public static UploadFilePart create(String name, StorageFileVO s) {
            return new UploadFilePart(name, s);
        }

        @Override
        public String getFileName() {
            return m_fileName;
        }

        @Override
        public boolean isFile() {
            return true;
        }
    }
}
