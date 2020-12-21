package com.infamous.zod.media.streaming.api;

import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaRange {

    private @Getter final int m_from;
    private @Getter final int m_to;
    private @Getter final int m_limit;

    public static MediaRange of(int from, int to, long limit) {
        if (from >= limit) {
            to = (int) limit - 1;
        }
        return new MediaRange(from, to, (int)limit);
    }

    public static MediaRange of(String rangeString, long limit) {
        String[] ranges = rangeString.split("=")[1].split("-");

        final int from = Integer.parseInt(ranges[0]);
        int to = 0;
        if (ranges.length == 2) {
            to = Integer.parseInt(ranges[1]);
        } else {
            to = MediaStreamingFrame.CHUNK_SIZE + from;
        }
        return of(from, to, limit);
    }

    public int calculateLength() {
        return m_to - m_from + 1;
    }

    public String generateRangeString() {
        return new StringBuilder()
            .append("bytes ")
            .append(this.getFrom()).append("-")
            .append(this.getTo()).append("/")
            .append(this.getLimit())
            .toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("from=").append(m_from);
        sb.append(", to=").append(m_to);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaRange that = (MediaRange) o;
        return m_from == that.m_from &&
            m_to == that.m_to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_from, m_to);
    }
}
