package com.infamous.zod.songmgmt.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class SongKey implements Serializable {

    private long id;

    public SongKey() {

    }

    public SongKey(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SongKey songKey = (SongKey) o;
        return id == songKey.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
