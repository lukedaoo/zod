package com.infamous.zod.storage.model;

import java.io.InputStream;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class StorageFileVO {

    private String m_id;
    private String m_fileName;
    private long m_fileSize;
    private String m_extension;
    private String m_downloadUrl;
    private boolean m_enabled;
    private InputStream m_content;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageFileVO that = (StorageFileVO) o;
        return Objects.equals(m_id, that.m_id) &&
            Objects.equals(m_fileName, that.m_fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_id, m_fileName);
    }
}
