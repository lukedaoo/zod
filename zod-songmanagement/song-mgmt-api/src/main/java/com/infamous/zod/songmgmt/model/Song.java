package com.infamous.zod.songmgmt.model;


import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "Song")
@Table(name = "Song")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String title;
    private String fileId;

    @Column(columnDefinition = "bit(1) default 1")
    private boolean enabled = true;

    public Long getId() {
        return id;
    }

    public Song setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Song setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getFileId() {
        return fileId;
    }

    public Song setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Song setEnabled(boolean enabled) {
        this.enabled = enabled;
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
        Song song = (Song) o;
        return id == song.id &&
            Objects.equals(title, song.title) &&
            Objects.equals(fileId, song.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, fileId);
    }
}
