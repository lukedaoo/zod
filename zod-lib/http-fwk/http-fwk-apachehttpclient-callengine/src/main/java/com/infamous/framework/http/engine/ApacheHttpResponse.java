package com.infamous.framework.http.engine;

import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.RawHttpResponse;
import com.infamous.framework.http.core.RawHttpResponseBase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

class ApacheHttpResponse extends RawHttpResponseBase implements RawHttpResponse {

    private final HttpResponse m_rawResponse;

    public ApacheHttpResponse(HttpResponse response) {
        m_rawResponse = response;
    }

    @Override
    public int getStatus() {
        return m_rawResponse.getStatusLine().getStatusCode();
    }

    @Override
    public String getStatusText() {
        return m_rawResponse.getStatusLine().getReasonPhrase();
    }

    @Override
    public Map<String, String> getHeaders() {

        Map<String, String> headers = new HashMap<>();

        for (Header header : m_rawResponse.getAllHeaders()) {
            headers.put(header.getName(), header.getValue());
        }

        return headers;
    }

    @Override
    public InputStream getContent() {
        try {
            HttpEntity entity = m_rawResponse.getEntity();
            if (entity != null) {
                return entity.getContent();
            }
            return new ByteArrayInputStream(new byte[0]);
        } catch (IOException e) {
            throw new ZodHttpException(e);
        }
    }

    @Override
    public byte[] getContentAsBytes() {
        try (InputStream initialStream = getContent()) {
            return initialStream.readAllBytes();
        } catch (IOException e) {
            throw new ZodHttpException(e);
        }
    }

    @Override
    public String getContentAsString() {
        if (!hasContent()) {
            return "";
        }
        try {
            return new String(getContentAsBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new ZodHttpException(e);
        }
    }

    @Override
    public boolean hasContent() {
        return m_rawResponse.getEntity() != null;
    }

    @Override
    public String getContentType() {
        if (hasContent()) {
            Header contentType = m_rawResponse.getEntity().getContentType();
            if (contentType != null) {
                return contentType.getValue();
            }
        }
        return "";
    }

    @Override
    public boolean isSuccess() {
        int status = getStatus();
        return status >= 200 && status < 400;
    }
}
