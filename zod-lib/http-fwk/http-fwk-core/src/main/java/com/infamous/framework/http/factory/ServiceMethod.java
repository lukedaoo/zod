package com.infamous.framework.http.factory;

import java.lang.reflect.Method;

abstract class ServiceMethod<RestResultType> {

    static <T> ServiceMethod<T> parseAnnotation(ZodHttpClientFactory clientFactory, Method method) {
        return AnnotationHandler.parseAnnotation(clientFactory, method);
    }

    public abstract RestResultType invoke(Object[] args) throws Exception;
}
