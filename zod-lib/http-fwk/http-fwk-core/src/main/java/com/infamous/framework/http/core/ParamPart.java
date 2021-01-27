package com.infamous.framework.http.core;

class ParamPart extends BodyPart<String> {

    public ParamPart(String name, Object value, String contentType) {
        super(name, value == null ? "" : String.valueOf(value), contentType);
    }

    @Override
    public boolean isFile() {
        return false;
    }
}
