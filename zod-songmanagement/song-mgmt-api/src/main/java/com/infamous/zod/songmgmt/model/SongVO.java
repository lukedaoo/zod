package com.infamous.zod.songmgmt.model;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class SongVO {

    private long m_id;
    private String m_title;
    @Builder.Default
    private boolean m_enabled = true;
    private String m_fileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SongVO songVO = (SongVO) o;
        return m_id == songVO.m_id &&
            Objects.equals(m_title, songVO.m_title) &&
            Objects.equals(m_fileId, songVO.m_fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_id, m_title, m_fileId);
    }
}
