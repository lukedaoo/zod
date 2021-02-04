package com.infamous.framework.http.engine;

import static org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE;

import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RequestBody;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

class ApacheHttpMapper {

    private ApacheHttpMapper() {

    }

    public static HttpEntity mapEntity(HttpRequest request) {
        Optional<RequestBody> bodyEntityOpt = request.getBody();

        return bodyEntityOpt
            .map(ApacheHttpMapper::doMapEntity)
            .orElse(null);
    }

    private static HttpEntity doMapEntity(RequestBody bodyEntity) {
        if (bodyEntity.isEmpty()) {
            return new BasicHttpEntity();
        }
        try {
            if (bodyEntity.isBodyEntity()) {
                return mapToBody(bodyEntity);
            } else {
                return mapToMultipart(bodyEntity);
            }
        } catch (Exception e) {
            throw new ZodHttpException(e);
        }

    }

    private static HttpEntity mapToBody(RequestBody bodyEntity) {
        BodyPart part = bodyEntity.entity();
        Charset charset = getCharset(bodyEntity);

        Object value = part.getValue();
        if (value instanceof byte[]) {
            return new ByteArrayEntity((byte[]) value);
        } else if (value instanceof InputStream) {
            return new InputStreamEntity((InputStream) value);
        } else if (value instanceof File) {
            return new FileEntity((File) value);
        } else {
            return new StringEntity((String) value, charset);
        }
    }

    private static HttpEntity mapToMultipart(RequestBody bodyMultipart) {
        Charset charset = getCharset(bodyMultipart);
        if (bodyMultipart.isMultiPart()) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(charset);
            builder.setMode(BROWSER_COMPATIBLE);
            for (BodyPart part : bodyMultipart.multiParts()) {
                builder.addPart(part.getName(), buildContentBody(part));
            }
            return builder.build();
        } else {
            return new UrlEncodedFormEntity(getList(bodyMultipart.multiParts()), charset);
        }
    }

    private static Charset getCharset(RequestBody requestBody) {
        Charset charset = requestBody.getCharset();
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return charset;
    }

    private static ContentBody buildContentBody(BodyPart part) {
        Object value = part.getValue();
        if (value instanceof byte[]) {
            return new ByteArrayBody((byte[]) value, toApacheType(part.getContentType()), part.getFileName());
        } else if (value instanceof InputStream) {
            return new InputStreamBody((InputStream) value, toApacheType(part.getContentType()), part.getFileName());
        } else if (value instanceof File) {
            return new FileBody((File) value, toApacheType(part.getContentType()), part.getFileName());
        } else {
            return new StringBody(String.valueOf(value), toApacheType(part.getContentType()));
        }
    }

    private static org.apache.http.entity.ContentType toApacheType(String type) {
        return org.apache.http.entity.ContentType.parse(type);
    }

    private static List<NameValuePair> getList(Collection<BodyPart> parameters) {
        List<NameValuePair> result = new ArrayList<>();
        for (BodyPart entry : parameters) {
            BasicNameValuePair value = new BasicNameValuePair(entry.getName(), String.valueOf(entry.getValue()));
            result.add(value);
        }
        return result;
    }
}
