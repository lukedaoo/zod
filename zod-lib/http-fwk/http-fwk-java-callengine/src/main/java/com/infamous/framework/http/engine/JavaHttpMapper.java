package com.infamous.framework.http.engine;

import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RequestBody;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

class JavaHttpMapper {

    private JavaHttpMapper() {
        throw new IllegalArgumentException();
    }

    static java.net.http.HttpRequest.Builder mapBody(java.net.http.HttpRequest.Builder builder, HttpRequest request) {
        String httpMethod = request.getMethod().name();
        Optional<RequestBody> body = request.getBody();
        return body
            .map(bodyEntity -> mapEntity(builder, httpMethod, bodyEntity))
            .orElse(builder.copy().method(httpMethod, BodyPublishers.noBody()));
    }

    private static Builder mapEntity(Builder builder, String httpMethod, RequestBody bodyEntity) {
        if (bodyEntity.isEmpty()) {
            return builder.method(httpMethod, BodyPublishers.noBody());
        }
        try {
            return doMapEntity(builder, httpMethod, bodyEntity);
        } catch (Exception e) {
            throw new ZodHttpException(e);
        }
    }

    private static java.net.http.HttpRequest.Builder doMapEntity(java.net.http.HttpRequest.Builder builder,
                                                                 String httpMethod, RequestBody bodyEntity)
        throws IOException {

        if (bodyEntity.isBodyEntity()) {
            return builder.method(httpMethod, ofEntityData(bodyEntity, bodyEntity.getCharset()));
        } else {
            String boundary = new BigInteger(256, new Random()).toString();
            Collection<BodyPart> parts = bodyEntity.multiParts();
            return builder
                .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                .method(httpMethod, ofMimeMultipartData(parts, boundary, bodyEntity.getCharset()));
        }
    }

    private static BodyPublisher ofMimeMultipartData(Collection<BodyPart> bodyPartCollection,
                                                     String boundary, Charset charset) throws IOException {
        charset = charset == null ? StandardCharsets.UTF_8 : charset;

        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(charset);
        for (BodyPart bodyPart : bodyPartCollection) {
            String name = bodyPart.getName();
            String contentType = bodyPart.getContentType();
            String fileName = bodyPart.getFileName();

            byte[] values = parseBodyPartValueToByteArray(bodyPart, charset);

            byteArrays.add(separator);

            byteArrays.add(("\"" + name + "\"; filename=\"" + fileName
                + "\"\r\nContent-Type: " + contentType + "\r\n\r\n").getBytes(charset));
            byteArrays.add(values);
            byteArrays.add("\r\n".getBytes(charset));

        }
        byteArrays.add(("--" + boundary + "--").getBytes(charset));
        return BodyPublishers.ofByteArrays(byteArrays);
    }

    private static byte[] parseBodyPartValueToByteArray(BodyPart bodyPart, Charset charset) throws IOException {
        Object bodyPartValue = bodyPart.getValue();
        if (bodyPartValue instanceof byte[]) {
            return (byte[]) bodyPartValue;
        } else if (bodyPartValue instanceof InputStream) {
            return ((InputStream) bodyPartValue).readAllBytes();
        } else if (bodyPartValue instanceof File) {
            File f = (File) bodyPartValue;
            return Files.readAllBytes(Path.of(f.getPath()));
        } else {
            return String.valueOf(bodyPart.getValue()).getBytes(charset);
        }
    }

    private static BodyPublisher ofEntityData(RequestBody bodyEntity, Charset charset) throws IOException {

        BodyPart part = bodyEntity.entity();

        Object value = part.getValue();
        if (value instanceof byte[]) {
            return BodyPublishers.ofByteArray((byte[]) value);
        } else if (value instanceof InputStream) {
            return BodyPublishers.ofInputStream(() -> (InputStream) value);
        } else if (value instanceof File) {
            Path path = Path.of(((File) value).getPath());
            return BodyPublishers.ofFile(path);
        } else {
            return BodyPublishers.ofString(String.valueOf(value), charset);
        }
    }
}
