package com.infamous.framework.http.engine;

import com.infamous.framework.http.core.HttpRequest;

public interface CallEngine {

    Call transformFrom(HttpRequest request);
}
