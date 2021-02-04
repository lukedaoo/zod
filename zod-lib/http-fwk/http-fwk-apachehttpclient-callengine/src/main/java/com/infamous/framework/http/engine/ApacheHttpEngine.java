package com.infamous.framework.http.engine;

import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.HttpRequest;
import java.lang.reflect.Type;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;

public class ApacheHttpEngine implements CallEngine {

    private static ApacheHttpEngine INSTANCE = new ApacheHttpEngine();

    private ApacheHttpEngine() {
    }

    public static ApacheHttpEngine getInstance() {
        return INSTANCE;
    }

    @Override
    public Call transform(Type returnType, HttpRequest request) {
        String url = request.getUrl();
        HttpRequestBase requestBase = ApacheHttpMethodFactory.create(request.getMethod(), url);

        Map<String, String> headers = request.getHeaders();
        headers.forEach(requestBase::addHeader);

        HttpEntity entity = ApacheHttpMapper.mapEntity(request);

        try {
            return new ApacheHttpCall(request, requestBase, entity);
        } catch (Exception e) {
            throw new ZodHttpException(e);
        }
    }
}
