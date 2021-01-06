package com.infamous.zod.storage.controller.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.infamous.framework.file.FileStorageException;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileRepository;
import com.infamous.zod.storage.repository.UploadResult;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageFileControllerImplTest {

    private StorageFileControllerImpl m_controller;
    private StorageFileRepository m_repo;

    @BeforeEach
    public void setup() {
        m_repo = mock(StorageFileRepository.class);
        m_controller = new StorageFileControllerImpl(m_repo);
    }

    @Test
    public void testUpload() {
        FormDataContentDisposition formDataContentDisposition = mock(FormDataContentDisposition.class);
        when(formDataContentDisposition.getFileName()).thenReturn("my_war.txt");

        when(m_repo.upload(any(StorageFileVO.class))).thenReturn(true);
        Response res = m_controller.uploadFile(mock(InputStream.class), formDataContentDisposition);
        assertNotNull(res);
        assertEquals(201, res.getStatus());
    }

    @Test
    public void testUpload_Fail() {
        FormDataContentDisposition formDataContentDisposition = mock(FormDataContentDisposition.class);
        when(formDataContentDisposition.getFileName()).thenReturn("my_war.txt");

        when(m_repo.upload(any(StorageFileVO.class))).thenReturn(false);
        Response res = m_controller.uploadFile(mock(InputStream.class), formDataContentDisposition);

        assertNotNull(res);
        assertEquals(500, res.getStatus());
        verify(m_repo).upload(any(StorageFileVO.class));
        verifyNoMoreInteractions(m_repo);
    }

    @Test
    public void testMultipleUpload() {
        List<FormDataBodyPart> bodies = new ArrayList<>();
        bodies.add(mockFormDataBodyPart("my_war.txt"));
        UploadResult uploadResult = new UploadResult();
        uploadResult.setStatus("success");
        when(m_repo.upload(any(Collection.class))).thenReturn(uploadResult);
        Response res = m_controller.uploadFile(bodies);

        assertNotNull(res);
        assertEquals(201, res.getStatus());
        assertEquals(uploadResult, res.getEntity());
        verify(m_repo).upload(any(Collection.class));
        verifyNoMoreInteractions(m_repo);
    }

    @Test
    public void testInfo_FileIdNotFound() {
        when(m_repo.find("file-id")).thenReturn(null);
        assertThrows(FileStorageException.class, () -> m_controller.info("file-id"));
    }

    @Test
    public void testInfo_Successfully() {
        when(m_repo.find("file-id")).thenReturn(mock(StorageFileVO.class));
        Response res = m_controller.info("file-id");

        assertNotNull(res);
        assertEquals(200, res.getStatus());
        verify(m_repo).find(eq("file-id"));
        verifyNoMoreInteractions(m_repo);
    }

    @Test
    public void testFindAll() {
        when(m_repo.findAll()).thenReturn(Arrays.asList(
            mockStorageFileVO("file-1"),
            mockStorageFileVO("file-2")
        ));

        Response res = m_controller.getAll();

        assertNotNull(res);
        assertEquals(200, res.getStatus());
        assertEquals(2, ((List) res.getEntity()).size());
        verify(m_repo).findAll();
        verifyNoMoreInteractions(m_repo);
    }

    @Test
    public void testFindAll_Fail() {
        when(m_repo.findAll()).thenReturn(null);

        assertThrows(FileStorageException.class, () -> m_controller.getAll());
    }

    private StorageFileVO mockStorageFileVO(String fileName) {
        return StorageFileVO.builder()
            .fileName(fileName)
            .build();
    }

    private FormDataBodyPart mockFormDataBodyPart(String fileName) {
        FormDataBodyPart mock = mock(FormDataBodyPart.class);
        when(mock.getEntityAs(InputStream.class)).thenReturn(mock(InputStream.class));

        ContentDisposition contentDisposition = mock(ContentDisposition.class);
        when(mock.getContentDisposition()).thenReturn(contentDisposition);
        when(contentDisposition.getFileName()).thenReturn(fileName);

        return mock;
    }
}