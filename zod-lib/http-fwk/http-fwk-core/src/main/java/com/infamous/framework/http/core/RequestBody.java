package com.infamous.framework.http.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

public interface RequestBody {

    boolean isBodyEntity();

    boolean isMultiPart();

    default Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    default Collection<BodyPart> multiParts() {
        return Collections.emptyList();
    }

    default BodyPart entity() {
        return null;
    }

    default boolean isEmpty() {
        if (isBodyEntity()) {
            return entity() == null;
        }
        if (isMultiPart()) {
            Collection<BodyPart> bodyParts = multiParts();
            return bodyParts == null || bodyParts.isEmpty();
        }
        return true;
    }
}
