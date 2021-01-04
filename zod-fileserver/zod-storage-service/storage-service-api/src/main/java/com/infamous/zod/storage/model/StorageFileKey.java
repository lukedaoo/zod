package com.infamous.zod.storage.model;

import java.io.Serializable;
import java.util.Objects;

public class StorageFileKey implements Serializable {

    private String id;

    public StorageFileKey() {

    }

    public StorageFileKey(String id) {
        this.id = id;
    }

    public String getId() {
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
        StorageFileKey that = (StorageFileKey) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
