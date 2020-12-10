package com.infamous.zod.ftp.model;

import java.util.Objects;
import lombok.Getter;

public class FTPUserName {

    private @Getter final String m_username;

    public FTPUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username is mandatory");
        }
        if (userName.length() < 4) {
            throw new IllegalArgumentException("Username must be at least 4 character");
        }
        for (int i = 0, len = userName.length(); i < len; i++) {
            char c = userName.charAt(i);
            if (c == '@') {
                throw new IllegalArgumentException("Username can not contain '@'");
            }
            if (c == '/') {
                throw new IllegalArgumentException("Username can not contain '/'");
            }
            if (c == '\\') {
                throw new IllegalArgumentException("Username can not contain '\\");
            }
        }
        this.m_username = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FTPUserName that = (FTPUserName) o;
        return Objects.equals(m_username, that.m_username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_username);
    }
}
