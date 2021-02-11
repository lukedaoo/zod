package com.infamous.zod.songmgmt.controller.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infamous.framework.converter.DefaultJsonConverter;
import com.infamous.zod.base.rest.entity.UploadResult;
import com.infamous.zod.songmgmt.controller.SongManagementController;
import com.infamous.zod.songmgmt.model.SongVO;
import com.infamous.zod.songmgmt.service.SongManagementService;
import com.infamous.zod.storage.model.StorageFileVO;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SongManagementControllerImpl implements SongManagementController {

    private SongManagementService m_songManagementService;

    @Autowired
    public SongManagementControllerImpl(SongManagementService songManagementService) {
        m_songManagementService = songManagementService;
    }

    @Override
    public Response upload(FormDataBodyPart songInfoPart, InputStream file, FormDataContentDisposition metadataFile) {

        SongVO songVO = convertToSong(songInfoPart);
        StorageFileVO storageFileVO = StorageFileVO.builder()
            .content(file)
            .fileName(metadataFile.getFileName())
            .build();

        SongVO result = m_songManagementService.upload(songVO, storageFileVO);
        return Response.ok(result).build();
    }

    @Override
    public Response multipleUpload(FormDataBodyPart songInfoParts, List<FormDataBodyPart> fileParts) {
        if (songInfoParts == null) {
            throw new NullPointerException("SongParts is null");
        }
        if (fileParts == null) {
            throw new NullPointerException("File Parts is null");
        }
        String songInfoAsString = songInfoParts.getValueAs(String.class);
        List<SongVO> songs = DefaultJsonConverter.getInstance()
            .readValue(songInfoAsString,
                new TypeReference<List<SongVO>>() {});
        if (songs.size() != fileParts.size()) {
            throw new RuntimeException("Missing song info or file");
        }

        List<StorageFileVO> files = convertToStorageFiles(fileParts);

        for (int i = 0, size = songs.size(); i < size; i++) {
            String fileName = files.get(i).getFileName();
            songs.get(i).setFileName(fileName);
        }

        UploadResult<SongVO> result = m_songManagementService.multipleUpload(songs, files);

        return Response.ok(result).build();
    }

    @Override
    public Response getAll() {
        return Response.ok(m_songManagementService.findAll()).build();
    }

    private StorageFileVO convertToStorageFile(FormDataBodyPart part) {
        InputStream is = part.getEntityAs(InputStream.class);
        ContentDisposition metadata = part.getContentDisposition();

        return StorageFileVO.builder()
            .fileName(metadata.getFileName())
            .content(is)
            .build();
    }

    private List<StorageFileVO> convertToStorageFiles(List<FormDataBodyPart> fileParts) {
        return fileParts.stream().map(this::convertToStorageFile).collect(Collectors.toList());
    }

    private SongVO convertToSong(FormDataBodyPart songInfo) {
        if (songInfo == null) {
            throw new RuntimeException();
        }
        songInfo.setMediaType(MediaType.APPLICATION_JSON_TYPE);
        return songInfo.getValueAs(SongVO.class);
    }
}
