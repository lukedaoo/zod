package com.infamous.zod.media.streaming.controller.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infamous.zod.media.streaming.api.MediaStreamingService;
import com.infamous.zod.media.streaming.service.MediaStreamingServiceImpl;
import com.infamous.zod.storage.repository.StorageFileRepository;
import java.io.File;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaStreamingControllerImplTest {

    private MediaStreamingControllerImpl m_controller;
    private StorageFileRepository m_storageFileRepository;
    private MediaStreamingService m_mediaStreamingService;


    private File m_file;

    @BeforeEach
    public void setup() {
        m_storageFileRepository = mock(StorageFileRepository.class);
        m_mediaStreamingService = new MediaStreamingServiceImpl();

        m_file = new File("src/test/resources/attackontitan/chars.txt");
        m_controller = new MediaStreamingControllerImpl(m_storageFileRepository, m_mediaStreamingService);
    }

    @Test
    public void testView() {
        when(m_storageFileRepository.findPhysicalFile("file-id")).thenReturn(m_file);
        Response res = m_controller.view("file-id", null);

        assertNotNull(res);
        assertEquals(206, res.getStatus());
        verify(m_storageFileRepository).findPhysicalFile(eq("file-id"));
    }

    @Test
    public void testView_WhenFindNoFound() {
        when(m_storageFileRepository.findPhysicalFile("file-id")).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> m_controller.view("file-id", null));
        verify(m_storageFileRepository).findPhysicalFile(eq("file-id"));
    }

    @Test
    public void testView_InvalidRangeString() {
        when(m_storageFileRepository.findPhysicalFile("file-id")).thenReturn(m_file);
        IllegalArgumentException exp = assertThrows(IllegalArgumentException.class,
            () -> m_controller.view("file-id", "byte-0"));

        assertEquals("The format of rangeString is invalid. Range: byte-0", exp.getMessage());
        verify(m_storageFileRepository).findPhysicalFile(eq("file-id"));
    }

    @Test
    public void testView_SuccessfullyWithRangeString() {
        assertTrue(m_file.length() > 200);
        when(m_storageFileRepository.findPhysicalFile("file-id")).thenReturn(m_file);
        Response res = m_controller.view("file-id", "bytes=0-200");
        verify(m_storageFileRepository).findPhysicalFile(eq("file-id"));

        assertNotNull(res);
        assertEquals(206, res.getStatus());
        String expectedRange = "bytes 0-200" + "/" + m_file.length();
        assertEquals(expectedRange, res.getHeaderString("Content-Range"));
    }
}