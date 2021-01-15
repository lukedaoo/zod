package com.infamous.framework.http.factory;

import com.infamous.framework.http.Async;
import com.infamous.framework.http.Body;
import com.infamous.framework.http.Header;
import com.infamous.framework.http.Headers;
import com.infamous.framework.http.MultiPart;
import com.infamous.framework.http.Part;
import com.infamous.framework.http.PathParam;
import com.infamous.framework.http.QueryParam;
import com.infamous.framework.http.Rest;
import com.infamous.framework.http.Url;
import com.infamous.framework.http.core.HttpRequestWithBody;
import com.infamous.framework.http.factory.ParameterHandler.Path;
import com.infamous.framework.http.factory.ParameterHandler.Query;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class AnnotationHandler {

    public static <RestResultType> ServiceMethod<RestResultType> parseAnnotation(ZodHttpClientFactory clientFactory,
                                                                                 Method method) {
        MethodAnnotationInfo methodAnnotationInfo = new MethodAnnotationInfo();
        State state = new State();

        HttpRequestWithBody request = extractAnnotationToBaseRequest(methodAnnotationInfo, state, clientFactory,
            method);

        List<ParameterHandler<?>> parameterHandlers = extractToParameterHandlers(clientFactory, method, state);

        if (state.isHasBody() && state.isUseMultiPartBody() && state.isUseEntityBody()) {
            throw Utils.methodError(method, "Use only @Body or @Multipart");
        }

        if (state.isUseMultiPartBody() && !state.isHasPart()) {
            throw Utils.methodError(method, "@Part is required when using @Multipart");
        }

        Type returnType = method.getGenericReturnType();

        if (returnType == void.class) {
            throw Utils.methodError(method, "Service methods cannot return void.");
        }

        if (state.isUseAsync() && returnType instanceof ParameterizedType) {
            if (((ParameterizedType) returnType).getRawType() != CompletableFuture.class) {
                throw Utils.methodError(method, "Service method async must return CompletableFuture");
            }
        }

        RequestFactory requestFactory = new RequestFactory(clientFactory, request, parameterHandlers);

        Type actualReturnType = state.isUseAsync() ? getActualReturnType(returnType) : returnType;

        return new HttpServiceMethod<>(requestFactory, actualReturnType, state.isUseAsync());
    }

    private static Type getActualReturnType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("");
        }
        if (type instanceof ParameterizedType) {
            if (((ParameterizedType) type).getRawType() == CompletableFuture.class) {
                return ((ParameterizedType) type).getActualTypeArguments()[0];
            }
        }
        return type;
    }


    private static List<ParameterHandler<?>> extractToParameterHandlers(ZodHttpClientFactory clientFactory,
                                                                        Method method, State state) {

        List<ParameterHandler<?>> parameterHandlers = new ArrayList<>();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Type[] parameterTypes = method.getParameterTypes();

        int countAnnotationBody = 0;
        for (int i = 0; i < paramAnnotations.length; i++) {
            Type type = parameterTypes[i];
            Annotation[] annotationArray = paramAnnotations[i];

            for (Annotation annotation : annotationArray) {
                ParameterHandler<?> parameterHandler = null;
                if (annotation instanceof Header) {
                    parameterHandler = new ParameterHandler.Header<>((Header) annotation,
                        clientFactory.stringConverter());
                } else if (annotation instanceof PathParam) {
                    parameterHandler = new Path<>((PathParam) annotation, clientFactory.stringConverter());
                } else if (annotation instanceof QueryParam) {
                    parameterHandler = new Query<>((QueryParam) annotation, clientFactory.stringConverter());
                } else if (annotation instanceof Part) {
                    if (!state.isHasPart()) {
                        state.hasPart();
                    }
                    parameterHandler = new ParameterHandler.Part<>((Part) annotation);
                } else if (annotation instanceof Body) {
                    state.useEntityBody();
                    countAnnotationBody++;

                    parameterHandler = new ParameterHandler.Body<>(clientFactory.requestBodyConverter(type));
                } else if (annotation instanceof Url) {
                    parameterHandler = new ParameterHandler.Url<>((Url) annotation, clientFactory.stringConverter());
                }
                if (countAnnotationBody > 1) {
                    throw Utils.parameterError(method, i, "Only use 1 @Body");
                }
                if (parameterHandler != null) {
                    parameterHandlers.add(parameterHandler);
                }
            }
        }
        return parameterHandlers;
    }

    private static HttpRequestWithBody extractAnnotationToBaseRequest(MethodAnnotationInfo methodAnnotationInfo,
                                                                      State state,
                                                                      ZodHttpClientFactory clientFactory,
                                                                      Method method) {
        Annotation[] annotations = method.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof Headers) {
                methodAnnotationInfo.m_headers = (Headers) annotation;
            } else if (annotation instanceof Rest) {
                methodAnnotationInfo.m_rest = (Rest) annotation;
            } else if (annotation instanceof MultiPart) {
                state.useMultipartBody();
            } else if (annotation instanceof Async) {
                state.useAsync();
            }
        }

        Map<String, String> headers = methodAnnotationInfo.extractHeaders(method);
        HttpRequestWithBody request = methodAnnotationInfo
            .extractToRequest(method, clientFactory.baseUrl(), clientFactory.config(), clientFactory.getObjectMapper());

        request.headers(headers);

        return request;
    }

    private static class State {

        boolean m_hasBody;
        boolean m_useMultiPartBody;
        boolean m_useEntityBody;

        boolean m_hasPart = false;

        boolean m_useAsync = false;

        void useMultipartBody() {
            m_useMultiPartBody = true;
            m_useEntityBody = false;
            m_hasBody = true;
        }

        void useEntityBody() {
            m_useMultiPartBody = false;
            m_useEntityBody = true;
            m_hasBody = true;
        }

        void useAsync() {
            m_useAsync = true;
        }

        boolean isHasBody() {
            return m_hasBody;
        }

        boolean isUseEntityBody() {
            return m_useEntityBody;
        }

        boolean isUseMultiPartBody() {
            return m_useMultiPartBody;
        }

        void hasPart() {
            m_hasPart = true;
        }

        boolean isHasPart() {
            return m_hasPart;
        }

        boolean isUseAsync() {
            return m_useAsync;
        }
    }

}
