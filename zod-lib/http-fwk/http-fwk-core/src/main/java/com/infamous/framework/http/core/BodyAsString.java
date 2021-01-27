package com.infamous.framework.http.core;

class BodyAsString extends BodyPart<String> {

    public BodyAsString(String value) {
        super(null, value, null);
    }

    @Override
    public boolean isFile() {
       return false;
    }
}
