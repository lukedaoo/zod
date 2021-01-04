package com.infamous.zod.ftp.model;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class FTPUserKey implements Serializable {

    private String username;

    public String getUsername() {
        return username;
    }

    public FTPUserKey() {

    }

    public FTPUserKey(String username) {
        this.username = username;
    }
}
