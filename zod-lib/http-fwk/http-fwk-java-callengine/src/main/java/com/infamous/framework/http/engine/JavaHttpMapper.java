package com.infamous.framework.http.engine;

import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyAsByteArray;
import com.infamous.framework.http.core.BodyAsString;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RequestBody;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

class JavaHttpMapper {


    static java.net.http.HttpRequest.Builder mapBody(java.net.http.HttpRequest.Builder builder, HttpRequest request) {
        String httpMethod = request.getMethod().name();
        Optional<RequestBody> body = request.getBody();
        return body
            .map(bodyEntity -> mapEntity(builder, httpMethod, bodyEntity))
            .orElse(builder);
    }

    private static java.net.http.HttpRequest.Builder mapEntity(java.net.http.HttpRequest.Builder builder,
                                                               String httpMethod, RequestBody bodyEntity) {
        if (bodyEntity.isBodyEntity()) {
            BodyPart part = bodyEntity.entity();
            if (bodyEntity.isEmpty()) {
                return builder.method(httpMethod, BodyPublishers.noBody());
            }
            if (part instanceof BodyAsString) {
                return builder.method(httpMethod,
                    BodyPublishers.ofString(((BodyAsString) part).getValue()));
            } else if (part instanceof BodyAsByteArray) {
                return builder.method(httpMethod,
                    BodyPublishers.ofByteArray(((BodyAsByteArray) part).getValue()));
            }
        } else if (bodyEntity.isMultiPart()) {
            String boundary = new BigInteger(256, new Random()).toString();
            Collection<BodyPart> parts = bodyEntity.multiParts();
            try {
                return builder
                    .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                    .method(httpMethod, ofMimeMultipartData(parts, boundary, bodyEntity.getCharset()));
            } catch (IOException e) {
                throw new ZodHttpException(e);
            }
        }
        return builder;
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
            Class<?> type = bodyPart.getPartType();
            byte[] values;

            if (type == byte[].class) {
                values = (byte[]) bodyPart.getValue();
            } else if (type == InputStream.class || type.getSuperclass() == InputStream.class) {
                values = readAllByte((InputStream) bodyPart.getValue());
            } else if (type == File.class) {
                File f = (File) bodyPart.getValue();
                values = Files.readAllBytes(Path.of(f.getPath()));
            } else {
                values = String.valueOf(bodyPart.getValue()).getBytes(charset);
            }

            byteArrays.add(separator);

            byteArrays.add(("\"" + name + "\"; filename=\"" + fileName
                + "\"\r\nContent-Type: " + contentType + "\r\n\r\n").getBytes(charset));
            byteArrays.add(values);
            byteArrays.add("\r\n".getBytes(charset));

        }
        byteArrays.add(("--" + boundary + "--").getBytes(charset));
        return BodyPublishers.ofByteArrays(byteArrays);
    }


    private static byte[] readAllByte(InputStream is) throws IOException {
        return is.readAllBytes();
    }
}
