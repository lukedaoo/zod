package com.infamous.framework.http.core;

import java.io.File;

class FilePart extends BodyPart<File> {

    private String m_fileName;

    public FilePart(File value, String name, String contentType) {
        super(value, name, contentType);
        m_fileName = value.getName();
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
