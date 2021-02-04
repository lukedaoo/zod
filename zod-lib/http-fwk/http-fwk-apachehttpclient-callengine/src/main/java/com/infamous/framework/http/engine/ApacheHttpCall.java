package com.infamous.framework.http.engine;

import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RawHttpResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

// It's broken. Pls don't use this shit
// TODO:
//   - Learn Apache Http Client && Apache Async Http Client
//   - Re-write to use reuse Http Client
class ApacheHttpCall implements Call {

    private final HttpRequest m_rawRequest;
    private final HttpEntity m_entity;
    private final HttpRequestBase m_request;

    private final HttpClientConnectionManager m_connectionManager;
    private final SSLConnectionSocketFactory m_sslConnectionSocketFactory;

    private final CloseableHttpClient m_client;
    private final CloseableHttpAsyncClient m_asyncClient;

    public ApacheHttpCall(HttpRequest rawRequest, HttpRequestBase request, HttpEntity entity)
        throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        this.m_rawRequest = rawRequest;
        this.m_request = request;
        this.m_entity = entity;

        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        m_sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
            RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", m_sslConnectionSocketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        m_connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);

        m_client = HttpClients
            .custom()
            .setSSLSocketFactory(m_sslConnectionSocketFactory)
            .setConnectionManager(m_connectionManager).build();

        m_asyncClient = HttpAsyncClientBuilder.create().build();
        m_asyncClient.start();
    }

    @Override
    public RawHttpResponse execute() {
        setEntity(false);

        try {
            CloseableHttpResponse response = m_client.execute(m_request);
            return new ApacheHttpResponse(response);
        } catch (IOException e) {
            throw new ZodHttpException(e);
        }
    }

    @Override
    public CompletableFuture<RawHttpResponse> executeAsync() {
        setEntity(true);

        CompletableFuture<RawHttpResponse> res = new CompletableFuture<>();

        m_asyncClient.execute(m_request, new FutureCallback<>() {
            @Override
            public void completed(HttpResponse response) {
                res.complete(new ApacheHttpResponse(response));
            }

            @Override
            public void failed(Exception e) {
                res.completeExceptionally(e);
            }

            @Override
            public void cancelled() {
                res.completeExceptionally(new ZodHttpException("canceled"));
            }
        });
        return res;
    }

    @Override
    public HttpRequestBase request() {
        return m_request;
    }

    @Override
    public HttpRequest rawRequest() {
        return m_rawRequest;
    }

    private void setEntity(boolean isAsync) {
        if (m_entity == null) {
            return;
        }
        if (!isAsync) {
            ((HttpEntityEnclosingRequestBase) m_request).setEntity(m_entity);
        }
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            m_entity.writeTo(output);
            NByteArrayEntity en = new NByteArrayEntity(output.toByteArray());
            ((HttpEntityEnclosingRequestBase) m_request).setEntity(en);
        } catch (IOException e) {
            throw new ZodHttpException(e);
        }
    }
}
