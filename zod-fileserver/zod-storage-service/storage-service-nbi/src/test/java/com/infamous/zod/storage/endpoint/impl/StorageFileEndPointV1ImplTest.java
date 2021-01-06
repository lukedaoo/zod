package com.infamous.zod.storage.endpoint.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.infamous.zod.base.rest.RestEndPoint;
import com.infamous.zod.storage.controller.StorageFileController;
import com.infamous.zod.storage.endpoint.StorageFileEndPointV1;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageFileEndPointV1ImplTest {

    private StorageFileEndPointV1 m_endPoint;
    private StorageFileController m_controller;

    @BeforeEach
    public void setup() {
        m_controller = mock(StorageFileController.class);
        m_endPoint = new StorageFileEndPointV1Impl(m_controller);
    }

    @Test
    public void testHasRestEndPointAnnotation() {

        Method[] methods = Arrays.stream(StorageFileEndPointV1Impl.class.getMethods())
            .filter(method -> Objects.nonNull(method.getAnnotation(RestEndPoint.class)))
            .toArray(Method[]::new);

        assertEquals(6, methods.length);

        Set<String> methodNames = Arrays.stream(methods)
            .map(Method::getName)
            .collect(Collectors.toSet());
        assertEquals(4, methodNames.size());
        assertTrue(methodNames.contains("download"));
        assertTrue(methodNames.contains("multipleDownload"));
        assertTrue(methodNames.contains("info"));
        assertTrue(methodNames.contains("uploadFile"));
    }


    @Test
    void testDownload() {
        m_endPoint.download("file-id");
        verify(m_controller).download(eq("file-id"));
        verifyNoMoreInteractions(m_controller);
    }

    @Test
    void testMultipleDownload() {
        List<String> ids = new ArrayList<>() {{
            add("123");
            add("456");
        }};
        m_endPoint.multipleDownload(mock(HttpServletRequest.class), ids);
        verify(m_controller).multipleDownload(any(HttpServletRequest.class), eq(ids));
        verifyNoMoreInteractions(m_controller);
    }

    @Test
    void testUploadFile() {
        m_endPoint.uploadFile(mock(List.class));
        verify(m_controller).uploadFile(any(List.class));
        verifyNoMoreInteractions(m_controller);
    }

    @Test
    public void testSingleFileUpload() {
        m_endPoint.uploadFile(mock(InputStream.class), mock(FormDataContentDisposition.class), 100);

        verify(m_controller).uploadFile(any(), any());
        verifyNoMoreInteractions(m_controller);
    }

    @Test
    void testInfo() {
        m_endPoint.info("file-id");

        verify(m_controller).info(eq("file-id"));
        verifyNoMoreInteractions(m_controller);
    }

    @Test
    void testGetAll() {
        m_endPoint.info();

        verify(m_controller).getAll();
        verifyNoMoreInteractions(m_controller);
    }
}