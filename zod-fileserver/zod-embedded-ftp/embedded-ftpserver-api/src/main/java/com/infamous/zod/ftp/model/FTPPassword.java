package com.infamous.zod.ftp.model;

import java.util.Objects;
import lombok.Getter;

public class FTPPassword {

    private @Getter final String m_password;

    public FTPPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is mandatory");
        }

        if (password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 character");
        }
        // https://www.ibm.com/support/knowledgecenter/SSLTBW_2.1.0/com.ibm.zos.v2r1.halg001/rdftpass.htm
        for (int i = 0, len = password.length(); i < len; i++) {
            char c = password.charAt(i);
            if (c == '/') {
                throw new IllegalArgumentException("Password can not contain '/'");
            }
        }

        this.m_password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FTPPassword that = (FTPPassword) o;
        return Objects.equals(m_password, that.m_password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_password);
    }
}
