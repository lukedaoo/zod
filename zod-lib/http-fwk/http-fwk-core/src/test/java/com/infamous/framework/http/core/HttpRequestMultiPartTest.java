package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.ObjectMapper;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpRequestMultiPartTest {


    private HttpRequestBody m_httpRequestBody;

    @BeforeEach
    public void setup() {
        m_httpRequestBody = mock(HttpRequestBody.class);

        when(m_httpRequestBody.getObjectMapper()).thenReturn(mock(ObjectMapper.class));
    }

    @Test
    public void testCreateHttpRequestMultipartBody() {
        HttpRequestMultiPart requestMultiPart = new HttpRequestMultiPart(m_httpRequestBody);

        requestMultiPart
            .part("file", mock(File.class), "application/file")
            .part("name", "ErenJeager", "text/plain")
            .part("inputStream", mock(InputStream.class), "application/input-stream", "aot.txt")
            .part("byteArr", new byte[0], "application/byte-array", "lalavidas.txt")
            .part(new BodyPart("inputStreamWithFileName", mock(InputStream.class), null) {
                @Override
                public boolean isFile() {
                    return true;
                }

                @Override
                public String getFileName() {
                    return "ok.txt";
                }
            })
            .charset(StandardCharsets.UTF_16);

        assertNotNull(requestMultiPart);
        assertEquals(StandardCharsets.UTF_16, requestMultiPart.getCharset());

        assertTrue(requestMultiPart.isMultiPart());
        assertFalse(requestMultiPart.isBodyEntity());
        assertFalse(requestMultiPart.isEmpty());
        assertEquals(5, requestMultiPart.getBody().get().multiParts().size());
    }

}