package com.infamous.framework.http.factory;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.core.RawHttpResponse;
import com.infamous.framework.http.engine.Call;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class HttpServiceMethod<T> extends ServiceMethod<T> {

    private final RequestFactory m_requestFactory;
    private final Type m_returnType;
    private final boolean m_useAsync;

    public HttpServiceMethod(RequestFactory requestFactory, Type returnType, boolean useAsync) {
        m_requestFactory = requestFactory;
        m_returnType = returnType;
        m_useAsync = useAsync;
    }

    @Override
    public T invoke(Object[] args) throws Exception {
        Call call = m_requestFactory.createCall(args);

        if (m_useAsync) {
            CompletableFuture<T> response = call.executeAsync()
                .thenApply(rawHttpResponse -> getConverter().converter(rawHttpResponse));
            return (T) response;
        } else {
            RawHttpResponse response = call.execute();
            return (T) getConverter().converter(response);
        }
    }

    private Converter<RawHttpResponse, T> getConverter() {
        return m_requestFactory.getClientFactory().responseBodyConverter(m_returnType);
    }
}
