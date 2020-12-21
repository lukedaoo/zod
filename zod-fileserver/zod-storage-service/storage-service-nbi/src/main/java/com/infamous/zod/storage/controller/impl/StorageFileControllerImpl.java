package com.infamous.zod.storage.controller.impl;

import com.infamous.framework.file.FileStorageException;
import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.storage.controller.StorageFileController;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileRepository;
import com.infamous.zod.storage.repository.UploadResult;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageFileControllerImpl implements StorageFileController {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(StorageFileControllerImpl.class, "storage.service");

    private StorageFileRepository m_repository;

    @Autowired
    public StorageFileControllerImpl(StorageFileRepository repository) {
        m_repository = repository;
    }

    @Override
    public Response download(String id) {
        StorageFileVO sf = m_repository.find(id);
        return Optional.ofNullable(sf)
            .map(file -> m_repository.download(file))
            .map(bytes -> toResponseFromByteArr(sf, bytes))
            .orElseThrow(() -> new FileStorageException("Not found file with id [" + id + "]"));
    }

    @Override
    public Response multipleDownload(HttpServletRequest request, List<String> ids) {
        return Optional.ofNullable(m_repository.find(ids))
            .filter(list -> !list.isEmpty())
            .map(list -> comressAndBuildResponse(request, list))
            .orElseThrow(() -> new FileStorageException("Not found files [" + ids + "]"));
    }

    @Override
    public Response uploadFile(InputStream content, FormDataContentDisposition metadata) {
        StorageFileVO s = StorageFileVO.builder()
            .fileName(metadata.getFileName())
            .content(content)
            .build();

        return m_repository.upload(s)
            ? Response.ok().status(Status.CREATED).build()
            : Response.status(500).build();
    }

    @Override
    public Response info(String id) {
        return Optional.ofNullable(m_repository.find(id))
            .map(f -> Response.ok(f).build())
            .orElseThrow(() -> new FileStorageException("Not found file with id [" + id + "]"));
    }

    @Override
    public Response uploadFile(List<FormDataBodyPart> bodyParts) {
        Set<StorageFileVO> set = parseBodyPartsToSetOfDto(bodyParts);
        UploadResult res = m_repository.upload(set);
        return Response.ok(res).status(Status.CREATED).build();
    }

    @Override
    public Response getAll() {
        return Optional.ofNullable(m_repository.findAll())
            .map(list -> Response.ok(list).build())
            .orElseThrow(() -> new FileStorageException("Not found any file"));
    }

    private Response toResponseFromByteArr(StorageFileVO sf, byte[] bytes) {
        ByteArrayInputStream ba = new ByteArrayInputStream(bytes);
        return Response
            .ok(ba)
            .header(HttpHeaders.CONTENT_LENGTH, bytes.length)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sf.getFileName() + "\"")
            .build();
    }

    private String generateCompressName(String sessionId) {
        return new StringBuilder("compressed-").append(sessionId).append(".zip").toString();
    }

    private void compress(List<StorageFileVO> files, java.io.OutputStream outputStream) {
        try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
            files.forEach(f -> compressSingleFile(out, f));
        } catch (IOException e) {
            LOGGER.error("Error while compressing files", e);
        }
    }

    private void compressSingleFile(ZipOutputStream out, StorageFileVO f) {
        byte[] data = m_repository.download(f);
        ZipEntry entry = new ZipEntry(f.getFileName());
        try {
            entry.setSize(data.length);
            out.putNextEntry(entry);
            out.write(data);
            out.closeEntry();
        } catch (IOException e) {
            LOGGER.error("Error while compressing file [" + f.getFileName() + "]", e);
        }
    }

    private Set<StorageFileVO> parseBodyPartsToSetOfDto(List<FormDataBodyPart> bodyParts) {
        Set<StorageFileVO> set = new HashSet<>(bodyParts.size());
        bodyParts.forEach(bodyPart -> {
            StorageFileVO sf = parseSingleBodyPart(bodyPart);
            set.add(sf);
        });
        return set;
    }

    private StorageFileVO parseSingleBodyPart(BodyPart part) {
        InputStream is = part.getEntityAs(InputStream.class);
        ContentDisposition metadata = part.getContentDisposition();

        return StorageFileVO.builder()
            .fileName(metadata.getFileName())
            .content(is)
            .build();
    }

    private Response comressAndBuildResponse(HttpServletRequest request, List<StorageFileVO> list) {
        StreamingOutput zipStream = outputStream -> compress(list, outputStream);
        String fileName = generateCompressName(request.getSession(true).getId());

        return Response
            .ok(zipStream)
            .type(MediaType.TEXT_PLAIN)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .build();
    }
}
