package com.infamous.framework.http.core;

public class BodyAsByteArray extends BodyPart<byte[]> {

    public BodyAsByteArray(byte[] value) {
        super(value, null, null);
    }

    @Override
    public boolean isFile() {
        return false;
    }
}
