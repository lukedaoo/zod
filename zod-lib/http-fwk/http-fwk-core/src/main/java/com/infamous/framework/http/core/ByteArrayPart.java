package com.infamous.framework.http.core;

class ByteArrayPart extends BodyPart<byte[]> {

    private final String m_fileName;

    ByteArrayPart(String name, byte[] bytes, String contentType, String fileName) {
        super(name, bytes, contentType);
        this.m_fileName = isEmpty(fileName) ? randomFileName(): fileName;
    }

    ByteArrayPart(String name, byte[] bytes, String contentType) {
        super(name, bytes, contentType);
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
