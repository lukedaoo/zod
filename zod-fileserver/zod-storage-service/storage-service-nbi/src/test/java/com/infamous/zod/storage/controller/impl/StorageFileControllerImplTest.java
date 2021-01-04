package com.infamous.zod.storage.controller.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.file.FileStorageException;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageFileControllerImplTest {

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
    }

    @Test
    public void testDownload_WhenFileNotFound_OnFileSystem_ThrowEx() {
        StorageFileVO vo = mockDTO();
        when(m_repo.find("1")).thenReturn(vo);
        when(m_repo.download(vo)).thenThrow(new FileStorageException("File not found"));

        FileStorageException exp = assertThrows(FileStorageException.class, () -> m_controller.download("1"));
        assertEquals("File not found", exp.getMessage());
    }

    @Test
    public void testDownload_WhenFileNotFound_OnFileSystem_ReturnNull() {
        StorageFileVO vo = mockDTO();
        when(m_repo.find("1")).thenReturn(vo);
        when(m_repo.download(vo)).thenReturn(null);

        FileStorageException exp = assertThrows(FileStorageException.class, () -> m_controller.download("1"));
        assertEquals("Not found file with id [1]", exp.getMessage());
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
    }


    public byte[] getFileAsByteArray(String fileLocation) {
        File file = new File(fileLocation);
        byte[] bytesArray = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytesArray);
        } catch (IOException e) {
            throw new FileStorageException("File not found");
        }
        return bytesArray;
    }

    private StorageFileVO mockDTO() {
        return StorageFileVO.builder()
            .fileName("my_war.txt")
            .id("1")
            .build();
    }

}