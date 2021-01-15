package com.infamous.framework.http.core;

import java.io.InputStream;
import java.util.Map;

public interface RawHttpResponse {

    int getStatus();

    String getStatusText();

    Map<String, String> getHeaders();

    InputStream getContent();

    byte[] getContentAsBytes();

    String getContentAsString();

    boolean hasContent();

    String getContentType();

    boolean isSuccess();
}
