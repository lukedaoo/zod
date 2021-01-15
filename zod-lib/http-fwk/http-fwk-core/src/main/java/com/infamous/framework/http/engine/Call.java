package com.infamous.framework.http.engine;

import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RawHttpResponse;
import java.util.concurrent.CompletableFuture;

public interface Call {

    RawHttpResponse execute();

    CompletableFuture<RawHttpResponse> executeAsync();

    // void execute(CallBack callbac);

    <RequestType> RequestType request();

    HttpRequest rawRequest();
}
