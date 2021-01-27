package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.Converter;
import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.core.RawHttpResponse;
import com.infamous.framework.http.engine.Call;
import com.infamous.framework.http.engine.CallEngine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpServiceMethodTest {


    private static final String ROOT = "src/test/resources";

    @BeforeEach
    public void setup() throws IOException {
        System.setProperty("java.io.tmpdir", ROOT + "/tmp");
        Files.createDirectory(Path.of(ROOT + "/tmp"));
    }

    @AfterEach
    public void tearDown() throws IOException {
        System.clearProperty("java.io.tmpdir");
        Files.walk(Path.of(ROOT + "/tmp"))
            .forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    //ignore
                }
            });

        Files.deleteIfExists(Path.of(ROOT + "/tmp"));
    }

    @Test
    public void testReturnInputStream() throws Exception {

        RequestFactory requestFactory = mock(RequestFactory.class);
        Call call = mockCall("jsonString");
        when(requestFactory.createCall(any(), any())).thenReturn(call);

        HttpServiceMethod<InputStream> httpServiceMethod = new HttpServiceMethod<>(requestFactory, InputStream.class,
            false);

        InputStream res = httpServiceMethod.invoke(new Object[]{"abc"});
        assertNotNull(res);
    }

    @Test
    public void testReturnFile() throws Exception {
        RequestFactory requestFactory = mockRequestFactory();
        Call call = mockCall("jsonString");
        when(requestFactory.createCall(any(), any())).thenReturn(call);

        HttpServiceMethod<File> httpServiceMethod = new HttpServiceMethod<>(requestFactory, File.class,
            false);

        File file = httpServiceMethod.invoke(new Object[]{"abc"});
        assertNotNull(file);
        assertEquals(new File(ROOT + "/aot.txt").length(), file.length());
    }

    @Test
    public void testReturnByteArray() throws Exception {
        RequestFactory requestFactory = mockRequestFactory();

        Call call = mockCall("jsonString");
        when(requestFactory.createCall(any(), any())).thenReturn(call);

        HttpServiceMethod<byte[]> httpServiceMethod = new HttpServiceMethod<>(requestFactory, byte[].class,
            false);

        byte[] bytes = httpServiceMethod.invoke(new Object[]{"abc"});
        assertNotNull(bytes);
        assertEquals(new File(ROOT + "/aot.txt").length(), bytes.length);
    }

    @Test
    public void testReturnObject() throws Exception {
        RequestFactory requestFactory = mockRequestFactory();

        Call call = mockCall("jsonString");
        when(requestFactory.createCall(any(), any())).thenReturn(call);

        HttpServiceMethod<String> httpServiceMethod = new HttpServiceMethod<>(requestFactory, String.class,
            false);

        String resString = httpServiceMethod.invoke(new Object[]{"abc"});
        assertNotNull(resString);
        assertEquals("jsonString", resString);
    }

    private RequestFactory mockRequestFactory() {
        RequestFactory requestFactory = mock(RequestFactory.class);

        Converter converter = mock(Converter.class);
        when(converter.converter(any())).thenAnswer(invocationOnMock -> {
            RawHttpResponse response = invocationOnMock.getArgument(0);

            return response.getContentAsString();
        });

        ConverterFactory converterFactory = mock(ConverterFactory.class);
        when(converterFactory.responseBodyConverter(any()))
            .thenReturn(converter);
        when(converterFactory.objectMapper()).thenReturn(mock(ObjectMapper.class));

        when(requestFactory.getClientFactory()).then(invocationOnMock -> {
            ZodHttpClientFactory zodHttpClientFactory = ZodHttpClientFactory
                .builder()
                .config(new HttpConfig())
                .callEngine(mock(CallEngine.class))
                .converterFactory(converterFactory)
                .build();
            return zodHttpClientFactory;
        });

        return requestFactory;
    }

    private Call mockCall(String contentAsString) throws FileNotFoundException {
        Call call = mock(Call.class);

        InputStream is = new FileInputStream(ROOT + "/aot.txt");

        RawHttpResponse response = mock(RawHttpResponse.class);
        when(response.getContent()).thenReturn(is);
        when(response.getContentAsBytes())
            .thenAnswer(invocationOnMock -> Files.readAllBytes(Path.of(ROOT + "/aot.txt")));
        when(response.getContentAsString()).thenReturn(contentAsString);

        when(call.execute()).thenReturn(response);
        when(call.executeAsync()).thenReturn(CompletableFuture.completedFuture(response));

        return call;
    }
}