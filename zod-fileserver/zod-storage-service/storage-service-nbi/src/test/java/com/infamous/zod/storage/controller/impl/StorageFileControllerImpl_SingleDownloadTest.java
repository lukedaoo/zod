package com.infamous.zod.storage.controller.impl;

import static com.infamous.zod.storage.controller.impl.CommonStorageFileControllerImpl.getFileAsByteArray;
import static com.infamous.zod.storage.controller.impl.CommonStorageFileControllerImpl.mockDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.infamous.framework.file.FileStorageException;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileRepository;
import java.io.InputStream;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StorageFileControllerImpl_SingleDownloadTest {

    private static final String ROOT = "src/test/resources";

    private StorageFileControllerImpl m_controller;
    private StorageFileRepository m_repo;

    @BeforeEach
    public void setup() {
        m_repo = mock(StorageFileRepository.class);
        m_controller = new StorageFileControllerImpl(m_repo);
    }

    @Test
    public void testDownload_WhenFileNotFound_OnDB() {
        when(m_repo.find("file-id")).thenReturn(null);
        FileStorageException exp = assertThrows(FileStorageException.class, () -> m_controller.download("file-id"));
        assertEquals("Not found file with id [file-id]", exp.getMessage());
        verify(m_repo).find(any(String.class));
        verifyNoMoreInteractions(m_repo);
    }

    @Test
    public void testDownload_WhenFileNotFound_OnFileSystem_ThrowEx() {
        StorageFileVO vo = mockDTO();
        when(m_repo.find("1")).thenReturn(vo);
        when(m_repo.download(vo)).thenThrow(new FileStorageException("File not found"));

        FileStorageException exp = assertThrows(FileStorageException.class, () -> m_controller.download("1"));
        assertEquals("File not found", exp.getMessage());
        verify(m_repo).find(any(String.class));
        verify(m_repo).download(any());
        verifyNoMoreInteractions(m_repo);
    }

    @Test
    public void testDownload_WhenFileNotFound_OnFileSystem_ReturnNull() {
        StorageFileVO vo = mockDTO();
        when(m_repo.find("1")).thenReturn(vo);
        when(m_repo.download(vo)).thenReturn(null);

        FileStorageException exp = assertThrows(FileStorageException.class, () -> m_controller.download("1"));
        assertEquals("Not found file with id [1]", exp.getMessage());
        verify(m_repo).find(any(String.class));
        verify(m_repo).download(any());
        verifyNoMoreInteractions(m_repo);
    }

    @Test
    public void testDownload() {
        StorageFileVO vo = mockDTO();
        when(m_repo.find("1")).thenReturn(vo);
        when(m_repo.download(vo)).thenReturn(getFileAsByteArray(ROOT + "/aot/" + vo.getFileName()));

        Response res = m_controller.download("1");
        assertNotNull(res);
        assertEquals(200, res.getStatus());
        assertTrue(res.getEntity() instanceof InputStream);
        assertTrue(res.getHeaderString(HttpHeaders.CONTENT_DISPOSITION).contains("filename=\"my_war.txt\""));
        verify(m_repo).find(any(String.class));
        verify(m_repo).download(any());
        verifyNoMoreInteractions(m_repo);
    }
}
