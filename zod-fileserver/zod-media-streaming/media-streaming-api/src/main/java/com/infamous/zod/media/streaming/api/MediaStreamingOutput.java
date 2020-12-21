package com.infamous.zod.media.streaming.api;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.framework.logging.core.LogLevel;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public final class MediaStreamingOutput implements StreamingOutput {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(MediaStreamingOutput.class, "media.streaming");

    private int m_length;
    private final String m_fileName;
    private final RandomAccessFile m_raf;
    private final byte[] m_buffer;

    public MediaStreamingOutput(int length, String fileName, RandomAccessFile raf) {
        this.m_length = length;
        this.m_raf = raf;
        this.m_fileName = fileName;
        this.m_buffer = new byte[4096];
    }

    public MediaStreamingOutput(int len, int from, File asset) throws IOException {
        this.m_length = len;
        this.m_fileName = asset.getName();
        this.m_raf = new RandomAccessFile(asset, "r");
        m_raf.seek(from);
        this.m_buffer = new byte[4096];
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        try {
            while (m_length != 0) {
                int read = m_raf.read(m_buffer, 0, Math.min(m_buffer.length, m_length));
                outputStream.write(m_buffer, 0, read);
                m_length -= read;
            }
        } catch (IOException e) {
            if (e.getMessage().contains("Broken pipe")) {
                LOGGER.trace("Broken pipe", e);
                return;
            }
            LOGGER.warn("Unexpected exception while streaming file", e);
        } finally {
            m_raf.close();
        }
    }

    public int getLength() {
        return m_length;
    }

    public String getFileName() {
        return m_fileName;
    }
}
