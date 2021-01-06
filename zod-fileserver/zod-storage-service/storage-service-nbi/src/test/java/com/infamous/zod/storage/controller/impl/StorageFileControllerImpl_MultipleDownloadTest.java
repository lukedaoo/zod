package com.infamous.zod.storage.controller.impl;

import static com.infamous.zod.storage.controller.impl.CommonStorageFileControllerImpl.getFileAsByteArray;
import static com.infamous.zod.storage.controller.impl.CommonStorageFileControllerImpl.mockDTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infamous.framework.file.FileStorageException;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageFileControllerImpl_MultipleDownloadTest {

    private static final String ROOT = "src/test/resources";

    private StorageFileControllerImpl m_controller;
    private StorageFileRepository m_repo;
    private HttpServletRequest m_httpServletRequest;

    @BeforeEach
    public void setup() {
        m_repo = mock(StorageFileRepository.class);
        m_httpServletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(m_httpServletRequest.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn("sessionId");
        m_controller = new StorageFileControllerImpl(m_repo);
    }

    @Test
    public void testDownload_WhenFileNotFound_OnDB() {
        when(m_repo.find(any(Collection.class))).thenReturn(null);

        FileStorageException exp = assertThrows(FileStorageException.class,
            () -> m_controller.multipleDownload(m_httpServletRequest, Arrays.asList("1", "2", "3")));

        assertEquals("Not found files with id(s) [1, 2, 3]", exp.getMessage());
        verify(m_repo).find(any(Collection.class));
    }

    @Test
    public void testDownload_WhenFileNotFound_OnDB_ReturnEmptyList() {
        when(m_repo.find(any(Collection.class))).thenReturn(Collections.emptyList());

        FileStorageException exp = assertThrows(FileStorageException.class,
            () -> m_controller.multipleDownload(m_httpServletRequest, Arrays.asList("1", "2", "3")));

        assertEquals("Not found files with id(s) [1, 2, 3]", exp.getMessage());
        verify(m_repo).find(any(Collection.class));
    }


    @Test
    public void testDownload_WhenFileNotFound_OnFileSystem_ThrowEx() {
        //Exception only throws when Response is going to write entity
        StorageFileVO vo = mockDTO();
        when(m_repo.find(Collections.singletonList("1"))).thenReturn(Collections.singletonList(vo));
        when(m_repo.download(vo)).thenThrow(new FileStorageException("File not found"));

        assertDoesNotThrow(() -> m_controller.multipleDownload(m_httpServletRequest, Collections.singletonList("1")));
    }

    @Test
    public void testDownload_WhenFileNotFound_OnFileSystem_ReturnNull() {
        StorageFileVO vo = mockDTO();
        when(m_repo.find(Collections.singletonList("1"))).thenReturn(Collections.singletonList(vo));
        when(m_repo.download(vo)).thenReturn(null);

        assertDoesNotThrow(() -> m_controller.multipleDownload(m_httpServletRequest, Collections.singletonList("1")));
        verify(m_repo).find(any(Collection.class));
    }

    @Test
    public void testDownload() {
        StorageFileVO vo1 = mockDTO("1");
        StorageFileVO vo2 = mockDTO("2");
        when(m_repo.find(any(Collection.class))).thenReturn(Arrays.asList(vo1, vo2));
        when(m_repo.download(any(StorageFileVO.class)))
            .thenReturn(getFileAsByteArray(ROOT + "/aot/my_war.txt"));

        Response res = m_controller.multipleDownload(m_httpServletRequest, Arrays.asList("1", "2"));
        assertNotNull(res);
        assertEquals(200, res.getStatus());
        assertTrue(res.getHeaderString(HttpHeaders.CONTENT_DISPOSITION).contains("sessionId"));
        verify(m_repo).find(any(Collection.class));
    }
}