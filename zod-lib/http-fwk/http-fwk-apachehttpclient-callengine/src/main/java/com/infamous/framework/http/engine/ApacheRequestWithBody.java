package com.infamous.framework.http.engine;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

class ApacheRequestWithBody extends HttpEntityEnclosingRequestBase {

    private String m_method;

    public ApacheRequestWithBody(String method, String uri) {
        this.m_method = method;
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return m_method;
    }
}
