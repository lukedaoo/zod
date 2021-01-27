package com.infamous.framework.http.core;

import java.io.File;

class FilePart extends BodyPart<File> {

    private String m_fileName;

    public FilePart(String name, File value, String contentType) {
        super(name, value, contentType);
        m_fileName = value.getName();
    }

    public FilePart(String name, File value, String contentType, String fileName) {
        super(name, value, contentType);
        m_fileName = isEmpty(fileName) ? value.getName() : fileName;
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
