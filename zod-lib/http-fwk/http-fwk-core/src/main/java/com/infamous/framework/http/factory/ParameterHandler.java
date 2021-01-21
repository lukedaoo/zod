package com.infamous.framework.http.factory;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.PathParam;
import com.infamous.framework.http.QueryParam;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.HttpRequestMultiPart;
import com.infamous.framework.http.core.HttpRequestWithBody;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

abstract class ParameterHandler<T> {

    public abstract HttpRequest apply(HttpRequest request, T value) throws Exception;

    static class Header<T> extends ParameterHandler<T> {

        private final String m_name;
        private final Converter<T, String> m_converter;

        public Header(com.infamous.framework.http.Header header, Converter<T, String> stringConverter) {
            m_name = header.value();
            m_converter = stringConverter;
        }

        @Override
        public HttpRequest apply(HttpRequest request, T value) throws Exception {
            return request.header(m_name, m_converter.converter(value));
        }
    }

    static class Path<T> extends ParameterHandler<T> {

        private final String m_name;
        private final boolean m_encoded;
        private final Converter<T, String> m_converter;

        public Path(PathParam pathParam, Converter<T, String> stringConverter) {
            m_name = pathParam.value();
            m_encoded = pathParam.encoded();
            m_converter = stringConverter;
        }

        @Override
        public HttpRequest apply(HttpRequest request, T value) throws Exception {
            return request.pathParam(m_name, m_converter.converter(value), m_encoded);
        }
    }

    static class Query<T> extends ParameterHandler<T> {

        private final String m_name;
        private final boolean m_encoded;
        private final Converter<T, String> m_converter;

        public Query(QueryParam queryParam, Converter<T, String> stringConverter) {
            m_name = queryParam.value();
            m_encoded = queryParam.encoded();
            m_converter = stringConverter;
        }

        @Override
        public HttpRequest apply(HttpRequest request, T value) throws Exception {
            if (value instanceof Collection) {
                return request.queryParam(m_name, (Collection<?>) value, m_encoded);
            } else {
                return request.queryParam(m_name, m_converter.converter(value), m_encoded);
            }
        }
    }

    static class Part<T> extends ParameterHandler<T> {

        private final String m_name;
        private final String m_contentType;
        private final String m_fileName;
        private final Map<Class<?>, HttpRequest> m_cache = new HashMap<>(2);

        public Part(com.infamous.framework.http.Part part) {
            m_name = part.value();
            m_contentType = part.contentType();
            // static file name
            m_fileName = part.fileName();
        }

        @Override
        public HttpRequest apply(HttpRequest request, T value) throws Exception {
            HttpRequest finalRequest = request;

            if (value instanceof Collection) {
                for (Object var : (Collection) value) {
                    finalRequest = appendPart(var, finalRequest);
                }
                return finalRequest;
            }

            finalRequest = appendPart(value, finalRequest);

            return finalRequest;
        }

        private HttpRequest appendPart(Object value, HttpRequest request) {

            if (value instanceof InputStream) {
                if (isHttpMultiPartRequest(request)) {
                    return castToHttpMultiPartRequest(request)
                        .part(m_name, (InputStream) value, m_contentType, m_fileName);
                } else {
                    return castToHttpRequestWithBody(request)
                        .part(m_name, (InputStream) value, m_contentType, m_fileName);
                }
            } else if (value instanceof byte[]) {
                if (isHttpMultiPartRequest(request)) {
                    return castToHttpMultiPartRequest(request).part(m_name, (byte[]) value, m_contentType, m_fileName);
                } else {
                    return castToHttpRequestWithBody(request).part(m_name, (byte[]) value, m_contentType, m_fileName);
                }
            } else if (value instanceof BodyPart) {
                if (isHttpMultiPartRequest(request)) {
                    return castToHttpMultiPartRequest(request).part((BodyPart) value);
                } else {
                    return castToHttpRequestWithBody(request).part((BodyPart) value);
                }
            } else {
                if (isHttpMultiPartRequest(request)) {
                    return castToHttpMultiPartRequest(request).part(m_name, value, m_contentType);
                } else {
                    return castToHttpRequestWithBody(request).part(m_name, value, m_contentType);
                }
            }
        }

        private boolean isHttpMultiPartRequest(HttpRequest request) {
            return request instanceof HttpRequestMultiPart;
        }

        private boolean isHttpRequestWithBody(HttpRequest request) {
            return !isHttpMultiPartRequest(request);
        }

        private HttpRequestMultiPart castToHttpMultiPartRequest(HttpRequest request) {
            return (HttpRequestMultiPart) request;
        }

        private HttpRequestWithBody castToHttpRequestWithBody(HttpRequest request) {
            return (HttpRequestWithBody) request;
        }
    }

    static class Body<T> extends ParameterHandler<T> {

        private Converter<T, BodyPart> m_requestBodyConverter;

        public Body(Converter<T, BodyPart> requestBodyConverter) {
            m_requestBodyConverter = requestBodyConverter;
        }

        @Override
        public HttpRequest apply(HttpRequest request, T value) throws Exception {
            if (request instanceof HttpRequestWithBody) {
                if (value instanceof String) {
                    return ((HttpRequestWithBody) request).body((String) value);
                } else if (value instanceof byte[]) {
                    return ((HttpRequestWithBody) request).body((byte[]) value);
                } else if (value instanceof BodyPart) {
                    return ((HttpRequestWithBody) request).body((BodyPart) value);
                } else {
                    return ((HttpRequestWithBody) request).body(m_requestBodyConverter.converter(value));
                }
            }
            throw new ZodHttpException(
                "HttpRequest type doesn't support @Body param. Type: " + request.getClass().getTypeName());
        }
    }

    static class Url<T> extends ParameterHandler<T> {

        private final Converter<T, String> m_converter;
        private final boolean m_forceUsingFullUrl;

        public Url(com.infamous.framework.http.Url annotation, Converter<T, String> stringConverter) {
            m_converter = stringConverter;
            m_forceUsingFullUrl = annotation.fullUrl();
        }

        @Override
        public HttpRequest apply(HttpRequest request, T value) throws Exception {
            if (m_forceUsingFullUrl) {
                return request.useUrl(m_converter.converter(value));
            }
            return request.appendUrl(m_converter.converter(value));
        }
    }
}
