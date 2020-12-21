package com.infamous.zod.media.streaming.api;

import java.util.Objects;
import lombok.Getter;

public class MediaStreamingFrame {

    public static final int CHUNK_SIZE = 1024 * 1024; // 1MB chunks

    private @Getter final MediaStreamingOutput m_output;
    private @Getter final MediaRange m_range;

    private MediaStreamingFrame(MediaStreamingOutput output, MediaRange range) {
        this.m_output = output;
        this.m_range = range;
    }

    public static MediaStreamingFrame of(MediaStreamingOutput output, MediaRange range) {
        return new MediaStreamingFrame(output, range);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaStreamingFrame that = (MediaStreamingFrame) o;
        return Objects.equals(m_output, that.m_output) &&
            Objects.equals(m_range, that.m_range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_output, m_range);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MediaStreamingFrame{");
        sb.append("file=").append(m_output.getFileName());
        sb.append(", range=").append(m_range);
        sb.append('}');
        return sb.toString();
    }
}
