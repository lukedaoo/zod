package com.infamous.framework.http.factory;

import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.engine.Call;
import com.infamous.framework.http.engine.CallEngine;
import java.util.List;

public class RequestFactory {

    private ZodHttpClientFactory m_clientFactory;
    private HttpRequest m_request;
    private ParameterHandler<Object>[] m_parameterHandlersArr;

    public RequestFactory(ZodHttpClientFactory factory, HttpRequest request,
                          List<ParameterHandler<?>> parameterHandlerList) {
        m_clientFactory = factory;
        m_request = request;
        m_parameterHandlersArr = parameterHandlerList.toArray(new ParameterHandler[0]);
    }

    public Call createCall(Object[] args) throws Exception {
        if (args != null) {
            for (int i = 0, argLength = args.length; i < argLength; i++) {
                if (i >= m_parameterHandlersArr.length) {
                    break;
                }
                m_request = m_parameterHandlersArr[i].apply(m_request, args[i]);
            }
        }
        CallEngine callEngine = m_clientFactory.getCallEngine();
        return callEngine.transformFrom(m_request);
    }

    public ZodHttpClientFactory getClientFactory() {
        return m_clientFactory;
    }
}
