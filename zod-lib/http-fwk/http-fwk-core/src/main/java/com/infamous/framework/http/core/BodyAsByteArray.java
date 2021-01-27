package com.infamous.framework.http.core;

class BodyAsByteArray extends BodyPart<byte[]> {

    public BodyAsByteArray(byte[] value) {
        super(null, value, null);
    }

    @Override
    public boolean isFile() {
        return false;
    }
}
