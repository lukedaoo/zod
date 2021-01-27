package com.infamous.framework.http.core;

import java.io.InputStream;

class InputStreamPart extends BodyPart<InputStream> {

    private String m_fileName;

    InputStreamPart(String name, InputStream value, String contentType, String fileName) {
        super(name, value, contentType);
        this.m_fileName = isEmpty(fileName) ? randomFileName(): fileName;
    }

    InputStreamPart(String name, InputStream value, String contentType) {
        super(name, value, contentType);
        this.m_fileName = randomFileName();
    }

    @Override
    public String getFileName() {
        return m_fileName;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s=%s", getName(), m_fileName);
    }
}
