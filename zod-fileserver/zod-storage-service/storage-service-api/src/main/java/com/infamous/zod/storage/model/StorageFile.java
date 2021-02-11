package com.infamous.zod.storage.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

@Entity(name = "StorageFile")
@Table(name = "StorageFile")
@IdClass(StorageFileKey.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String fileName;
    private long fileSize;
    private String extension;
    @Column(columnDefinition = "bit(1) default 1")
    private boolean enabled = true;
    private String checksum;

    public String getId() {
        return id;
    }

    public StorageFile setId(String id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public StorageFile setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public StorageFile setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public StorageFile setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public StorageFile setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public StorageFile setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageFile that = (StorageFile) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName);
    }
}
