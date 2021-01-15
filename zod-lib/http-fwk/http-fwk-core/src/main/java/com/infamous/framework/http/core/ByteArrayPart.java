package com.infamous.framework.http.core;

class ByteArrayPart extends BodyPart<byte[]> {

    private final String fileName;

    ByteArrayPart(String name, byte[] bytes, String contentType, String fileName) {
        super(bytes, name, contentType);
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s=%s", getName(), fileName);
    }
}
