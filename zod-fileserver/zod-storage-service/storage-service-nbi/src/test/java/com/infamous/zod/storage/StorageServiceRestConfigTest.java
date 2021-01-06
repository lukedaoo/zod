package com.infamous.zod.storage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.infamous.zod.base.rest.JerseyRestConfig;
import com.infamous.zod.media.streaming.controller.MediaStreamingController;
import com.infamous.zod.media.streaming.endpoint.MediaStreamingEndPointV1;
import com.infamous.zod.storage.controller.StorageFileController;
import com.infamous.zod.storage.endpoint.StorageFileEndPointV1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageServiceRestConfigTest {

    private StorageServiceRestConfig m_restConfig;

    @BeforeEach
    public void setup() {
        m_restConfig = new StorageServiceRestConfig();
    }

    @Test
    public void testCreateStorageEndPointV1() {
        assertNotNull(m_restConfig.createStorageFileEndpoint(mock(StorageFileController.class)));
    }

    @Test
    public void testCreateMediaStreamingEndPointV1() {
        assertNotNull(m_restConfig.createMediaStreamingEndpoint(mock(MediaStreamingController.class)));
    }

    @Test
    public void testCreateJerseyConfig() {
        StorageFileEndPointV1 sfEndPoint = mock(StorageFileEndPointV1.class);
        MediaStreamingEndPointV1 msEndPoint = mock(MediaStreamingEndPointV1.class);

        JerseyRestConfig jerseyRestConfig = m_restConfig.restConfig(sfEndPoint, msEndPoint);

        assertNotNull(jerseyRestConfig);
        assertTrue(jerseyRestConfig.isRegistered(sfEndPoint));
        assertTrue(jerseyRestConfig.isRegistered(msEndPoint));
    }
}