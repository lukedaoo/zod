package com.infamous.zod.media.streaming.api;

import java.io.IOException;
import java.io.InputStream;

public class SeekableByteArrayInputStream extends InputStream {

    private volatile byte[] m_buffer;
    private int m_cur;
    private int m_max;

    public SeekableByteArrayInputStream(byte[] buf) {
        this.m_buffer = buf;
        this.m_cur = 0;
        this.m_max = buf.length;
    }

    public SeekableByteArrayInputStream(byte[] buf, int maxOffset) {
        this.m_buffer = buf;
        this.m_cur = 0;
        this.m_max = maxOffset;
    }

    @Override
    public int read() {
        if (m_cur < m_max) {
            return m_buffer[m_cur++] & 0xff;
        } else {
            return -1;
        }
    }

    @Override
    public int read(byte[] b, int offset, int length) {
        if (b == null) {
            throw new NullPointerException();
        }

        if (length < 0 || offset < 0 || length > b.length - offset) {
            throw new IndexOutOfBoundsException();
        }

        if (length == 0) {
            return 0;
        }

        int avail = m_max - m_cur;

        if (avail <= 0) {
            return -1;
        }

        if (length > avail) {
            length = avail;
        }

        System.arraycopy(m_buffer, m_cur, b, offset, length);
        m_cur += length;
        return length;
    }

    @Override
    public long skip(long requestedSkip) {
        int actualSkip = m_max - m_cur;
        if (requestedSkip < actualSkip) {
            if (requestedSkip < 0) {
                actualSkip = 0;
            } else {
                actualSkip = (int) requestedSkip;
            }
        }

        m_cur += actualSkip;
        return actualSkip;
    }

    @Override
    public int available() {
        return m_max - m_cur;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized void mark(int readAheadLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        // See ByteArrayInputStream#close()
    }

    public void seek(int position) {
        if (position < 0 || position >= m_max) {
            throw new IllegalArgumentException("position = " + position + " maxOffset = " + m_max);
        }
        this.m_cur = position;
    }

    public int getPosition() {
        return this.m_cur;
    }

    byte[] getBuffer() {
        return m_buffer;
    }
}
