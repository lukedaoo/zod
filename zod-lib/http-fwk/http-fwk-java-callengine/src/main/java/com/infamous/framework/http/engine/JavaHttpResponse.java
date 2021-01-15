package com.infamous.framework.http.engine;

import com.infamous.framework.http.core.RawHttpResponse;
import com.infamous.framework.http.core.RawHttpResponseBase;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class JavaHttpResponse extends RawHttpResponseBase implements RawHttpResponse {

    private HttpResponse<?> m_response;

    public JavaHttpResponse(HttpResponse<?> response) {
        m_response = response;
    }

    @Override
    public int getStatus() {
        return m_response.statusCode();
    }

    @Override
    public String getStatusText() {
        return String.valueOf(m_response.body());
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> res = new HashMap<>();
        m_response.headers().map().forEach((k, v) -> res.put(k, v.get(0)));
        return res;
    }

    @Override
    public InputStream getContent() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public byte[] getContentAsBytes() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public String getContentAsString() {
        return String.valueOf(m_response.body());
    }

    @Override
    public boolean hasContent() {
        return getContentAsString() != null;
    }

    @Override
    public String getContentType() {
        return m_response.headers().firstValue("Content-Type").orElse("");
    }

    @Override
    public boolean isSuccess() {
        int status = getStatus();
        return status >= 200 && status < 400;
    }
}
