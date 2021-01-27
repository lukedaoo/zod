package com.infamous.framework.http.core;

import java.io.File;

class BodyAsFile extends BodyPart<File> {

    protected BodyAsFile(File value) {
        super(null, value, null);
    }

    @Override
    public boolean isFile() {
        return false;
    }
}
