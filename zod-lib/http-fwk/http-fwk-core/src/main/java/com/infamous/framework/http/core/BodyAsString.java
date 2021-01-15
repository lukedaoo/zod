package com.infamous.framework.http.core;

public class BodyAsString extends BodyPart<String> {

    public BodyAsString(String value) {
        super(value, null, null);
    }

    @Override
    public boolean isFile() {
       return false;
    }
}
