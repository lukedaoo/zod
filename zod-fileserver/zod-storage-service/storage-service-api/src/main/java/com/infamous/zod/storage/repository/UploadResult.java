package com.infamous.zod.storage.repository;

import com.infamous.zod.storage.model.StorageFileVO;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadResult {

    private String m_status;
    private List<StorageFileVO> m_data;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UploadResult that = (UploadResult) o;
        return Objects.equals(m_status, that.m_status) &&
            Objects.equals(m_data, that.m_data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_status, m_data);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UploadResult{");
        sb.append(" status='").append(m_status).append('\'');
        sb.append(", data=").append(m_data);
        sb.append('}');
        return sb.toString();
    }
}
