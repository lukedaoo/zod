package com.infamous.zod.storage.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.InputStream;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties({"fileSize", "extension", "content"})
public class StorageFileVO {

    private String m_id;
    private String m_fileName;
    private long m_fileSize;
    private String m_extension;
    private String m_checksum;
    @Builder.Default
    private boolean m_enabled = true;
    private InputStream m_content;

    public StorageFileVO(StorageFileVO storageFile) {
        m_id = storageFile.m_id;
        m_fileName = storageFile.m_fileName;
        m_fileSize = storageFile.m_fileSize;
        m_extension = storageFile.m_extension;
        m_checksum = storageFile.m_checksum;
        m_enabled = storageFile.m_enabled;
        m_content = storageFile.m_content;
    }

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
