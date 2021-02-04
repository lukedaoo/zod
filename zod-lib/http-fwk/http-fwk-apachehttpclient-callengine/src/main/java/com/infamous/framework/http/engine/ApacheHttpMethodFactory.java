package com.infamous.framework.http.engine;

import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.ZodHttpException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;

class ApacheHttpMethodFactory {

    private ApacheHttpMethodFactory() {

    }
    //GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD, TRACE;

    public static HttpRequestBase create(HttpMethod method, String url) {
        if (method == HttpMethod.GET) {
            return new HttpGet(url);
        }
        if (method == HttpMethod.POST) {
            return new HttpPost(url);
        }
        if (method == HttpMethod.PUT) {
            return new HttpPut(url);
        }
        if (method == HttpMethod.DELETE) {
            return new ApacheRequestWithBody(method.name(), url);
        }
        if (method == HttpMethod.PATCH) {
            return new ApacheRequestWithBody(method.name(), url);
        }
        if (method == HttpMethod.OPTIONS) {
            return new HttpOptions(url);
        }
        if (method == HttpMethod.HEAD) {
            return new HttpHead(url);
        }
        if (method == HttpMethod.TRACE) {
            return new HttpTrace(url);
        }
        throw new ZodHttpException("Unsupported method. Found: " + method);
    }


}
