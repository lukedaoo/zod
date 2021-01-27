package com.infamous.framework.http.core;

import com.infamous.framework.converter.ObjectMapper;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public class BodyPartFactory {

    private BodyPartFactory() {
        throw new IllegalArgumentException();
    }

    // ============== BodyEntity ============== //

    public static BodyPart<String> body(String value) {
        return new BodyAsString(value);
    }

    public static BodyPart<byte[]> body(byte[] bytes) {
        return new BodyAsByteArray(bytes);
    }

    public static BodyPart<InputStream> body(InputStream is) {
        return new BodyAsInputStream(is);
    }

    public static BodyPart<File> body(File file) {
        return new BodyAsFile(file);
    }

    public static BodyPart<?> body(BodyPart bodyPart) {
        return bodyPart;
    }

    public static BodyPart<?> body(Object value) {
        if (value instanceof String) {
            return body((String) value);
        }
        if (value instanceof byte[]) {
            return body((byte[]) value);
        }
        if (value instanceof InputStream) {
            return body((InputStream) value);
        }
        if (value instanceof File) {
            return body((File) value);
        }
        if (value instanceof BodyPart) {
            return body((BodyPart<?>) value);
        }
        return null;
    }

    public static BodyPart<?> body(Object value, ObjectMapper objectMapper) {
        Objects.requireNonNull(objectMapper, "objectMapper == null");
        BodyPart<?> res = body(value);
        return res == null ? body(objectMapper.writeValue(value)) : res;
    }

    // ============== Multipart ============== //

    public static BodyPart<InputStream> part(String name, InputStream value, String contentType) {
        return new InputStreamPart(name, value, contentType);
    }

    public static BodyPart<InputStream> part(String name, InputStream value, String contentType, String fileName) {
        return new InputStreamPart(name, value, contentType, fileName);
    }

    public static BodyPart<File> part(String name, File value, String contentType) {
        return new FilePart(name, value, contentType);
    }

    public static BodyPart<File> part(String name, File value, String contentType, String fileName) {
        return new FilePart(name, value, contentType, fileName);
    }

    public static BodyPart<byte[]> part(String name, byte[] value, String contentType, String fileName) {
        return new ByteArrayPart(name, value, contentType, fileName);
    }

    public static BodyPart<byte[]> part(String name, byte[] value, String contentType) {
        return new ByteArrayPart(name, value, contentType);
    }

    public static BodyPart<String> part(String name, String value, String contentType) {
        return new ParamPart(name, value, contentType);
    }

    public static BodyPart<?> part(BodyPart<?> bodyPart) {
        return bodyPart;
    }

    public static BodyPart<?> part(String name, Object value, String contentType) {
        return part(name, value, contentType, Optional.empty());
    }

    public static BodyPart<?> part(String name, Object value, String contentType, Optional<String> fileNameOpt) {
        String fileName = fileNameOpt.orElse("");
        if (value instanceof InputStream) {
            return part(name, (InputStream) value, contentType, fileName);
        }
        if (value instanceof File) {
            return part(name, (File) value, contentType, fileName);
        }
        if (value instanceof byte[]) {
            return part(name, (byte[]) value, contentType, fileName);
        }
        if (value instanceof String) {
            return part(name, (String) value, contentType);
        }
        if (value instanceof BodyPart) {
            return part((BodyPart) value);
        }
        return part(name, String.valueOf(value), contentType);
    }
}
