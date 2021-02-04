package com.infamous.zod.songmgmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SongVO {

    private Long m_id;
    private String m_title;
    @Builder.Default
    private boolean m_enabled = true;
    private String m_fileId;
    private String m_fileName;

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
