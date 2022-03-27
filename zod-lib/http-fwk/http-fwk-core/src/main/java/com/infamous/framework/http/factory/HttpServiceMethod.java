package com.infamous.framework.http.factory;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.RawHttpResponse;
import com.infamous.framework.http.engine.Call;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

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
        Call call = m_requestFactory.createCall(m_returnType, args);
        if (call == null) {
            throw new ZodHttpException("Call is null");
        }

        if (m_useAsync) {
            CompletableFuture<T> response = call.executeAsync().thenApply(this::doConvert);
            return (T) response;
        } else {
            RawHttpResponse response = call.execute();
            return (T) doConvert(response);
        }
    }

    private T doConvert(RawHttpResponse response) {
        if (m_returnType == byte[].class) {
            return (T) response.getContentAsBytes();
        }
        if (m_returnType == InputStream.class) {
            return (T) response.getContent();
        }
        if (m_returnType == File.class) {
            InputStream is = response.getContent();
            File file = createTempFile();
            copyInputStreamToFile(is, file);
            return (T) file;
        }
        return getConverter().converter(response);
    }

    private Converter<RawHttpResponse, T> getConverter() {
        return m_requestFactory.getClientFactory().responseBodyConverter(m_returnType);
    }

    private File createTempFile() {
        String strTmp = System.getProperty("java.io.tmpdir");
        String uuid = UUID.randomUUID().toString();
        return new File(strTmp + "/" + uuid + ".tmp");
    }

    private void copyInputStreamToFile(InputStream inputStream, File file) {

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (Exception e) {
            throw new ZodHttpException(e);
        }

    }
}
