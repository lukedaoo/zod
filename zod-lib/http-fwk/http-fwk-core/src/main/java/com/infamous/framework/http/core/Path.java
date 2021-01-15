package com.infamous.framework.http.core;

import com.infamous.framework.http.ZodHttpException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

class Path {

    private String m_rawUrl;
    private StringBuilder m_relativeUrl;

    private boolean m_isFirstQueryParam = true;

    public Path(String baseUrl, String url) {
        check(url == null, new IllegalArgumentException("url can not be NULL"));
        if (baseUrl != null) {
            baseUrl = baseUrl.toLowerCase();
            check(!baseUrl.startsWith("http") && !baseUrl.startsWith("https"),
                new IllegalArgumentException("Base url must be start with http or https"));
        }
        if (baseUrl == null) {
            if (!isStartWithProtocol(url)) {
                throw new ZodHttpException("url must start with protocol (http or https)");
            }
            this.m_rawUrl = url;
        } else {
            this.m_rawUrl = new StringBuilder().append(baseUrl).append(url).toString();
        }
        this.m_relativeUrl = new StringBuilder().append(m_rawUrl);
    }

    public Path(String url) {
        this(null, url);
    }

    public void appendQueryParam(String key, Collection<?> value, boolean encoded) {
        for (Object v : value) {
            appendQueryParam(key, v, encoded);
        }
    }

    public void appendQueryParam(String key, Object value, boolean encoded) {
        StringBuilder queryString = new StringBuilder();
        if (m_isFirstQueryParam) {
            m_isFirstQueryParam = false;
            queryString.append("?");
        } else {
            queryString.append("&");
        }
        key = encoded ? encode(key) : key;
        String valueAsString = encoded ? encode(String.valueOf(value)) : String.valueOf(value);

        queryString.append(key.trim());
        if (value != null) {
            queryString.append("=").append(valueAsString.trim());
        }

        m_relativeUrl.append(queryString);
    }

    public void appendPathParam(String key, String value, boolean encoded) {
        String pathParamPattern = new StringBuilder().append("{")
            .append(key).append("}").toString();
        int index = m_relativeUrl.indexOf(pathParamPattern);
        if (index != -1) {
            m_relativeUrl.replace(index, index + pathParamPattern.length(), value);
        } else {
            throw new IllegalArgumentException("@PathParam must has format {path}. Invalid for path: " + key);
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public String getUrl() {
        return m_relativeUrl.toString();
    }

    private boolean isStartWithProtocol(String url) {
        url = url.toLowerCase();
        return url.startsWith("http") || url.startsWith("https");
    }

    private void check(boolean condition, Exception e) {
        if (condition) {
            throw new ZodHttpException(e);
        }
    }

    public void appendUrl(String url) {
        m_relativeUrl.append(url);
        m_rawUrl += url;
    }

    public void useUrl(String url) {
        m_relativeUrl = new StringBuilder().append(url);
        m_rawUrl = url;
    }
}
