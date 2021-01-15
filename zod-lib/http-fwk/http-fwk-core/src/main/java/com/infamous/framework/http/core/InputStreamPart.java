package com.infamous.framework.http.core;

import java.io.InputStream;

class InputStreamPart extends BodyPart<InputStream> {

    private String fileName;

    InputStreamPart(String name, InputStream value, String contentType) {
        super(value, name, contentType);
    }

    InputStreamPart(String name, InputStream value, String contentType, String fileName) {
        super(value, name, contentType);
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
