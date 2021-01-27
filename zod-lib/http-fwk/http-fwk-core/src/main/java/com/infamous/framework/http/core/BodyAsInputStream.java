package com.infamous.framework.http.core;

import java.io.InputStream;

class BodyAsInputStream extends BodyPart<InputStream> {

    protected BodyAsInputStream(InputStream value) {
        super(null, value, null);
    }

    @Override
    public boolean isFile() {
        return false;
    }
}
