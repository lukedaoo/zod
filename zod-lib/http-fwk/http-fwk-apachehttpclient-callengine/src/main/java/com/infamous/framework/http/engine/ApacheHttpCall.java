package com.infamous.framework.http.engine;

import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RawHttpResponse;
import java.util.concurrent.CompletableFuture;

// TODO: so lazy
public class ApacheHttpCall implements Call {


    @Override
    public RawHttpResponse execute() {
        return null;
    }

    @Override
    public CompletableFuture<RawHttpResponse> executeAsync() {
        return null;
    }

    @Override
    public <RequestType> RequestType request() {
        return null;
    }

    @Override
    public HttpRequest rawRequest() {
        return null;
    }
}
