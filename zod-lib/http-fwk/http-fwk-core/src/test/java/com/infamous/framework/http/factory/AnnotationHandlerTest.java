package com.infamous.framework.http.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.ObjectMapper;
import com.infamous.framework.http.HttpConfig;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.engine.CallEngine;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationHandlerTest {

    private ZodHttpClientFactory m_factory;

    @BeforeEach
    public void setup() {
        ConverterFactory mockConverter = mock(ConverterFactory.class);
        when(mockConverter.getObjectMapper()).thenReturn(mock(ObjectMapper.class));
        m_factory = ZodHttpClientFactory.builder()
            .baseUrl("http://localhost:8080")
            .config(new HttpConfig())
            .converterFactory(mockConverter)
            .callEngine(mock(CallEngine.class))
            .build();
    }

    @Test
    public void testParseAnnotation_BlockingWithBody() throws Exception {
        Method method = getMethod("blockingRestWithBody", String.class, String.class, String.class, Integer.class,
            String.class);
        HttpServiceMethod serviceMethod = (HttpServiceMethod) AnnotationHandler.parseAnnotation(m_factory, method);

        assertNotNull(serviceMethod);
    }

    @Test
    public void testParseAnnotation_BlockingWith2Body() throws Exception {

        Method method = getMethod("blockingRestWith2Body", String.class, String.class);
        Exception exp = assertThrows(ZodHttpException.class,
            () -> AnnotationHandler.parseAnnotation(m_factory, method));
        assertEquals("Only use 1 @Body (parameter #2) for method RestClientTest.blockingRestWith2Body",
            exp.getMessage());
    }

    @Test
    public void testParseAnnotation_BlockingWithBodyAndMultipart() throws Exception {
        Method method = getMethod("blockingRestWithBodyAndMultiPart", String.class, String.class);
        Exception exp = assertThrows(ZodHttpException.class,
            () -> AnnotationHandler.parseAnnotation(m_factory, method));
        assertEquals("Use only @Body or @Multipart for method RestClientTest.blockingRestWithBodyAndMultiPart",
            exp.getMessage());
    }


    @Test
    public void testParseAnnotation_BlockingWithMultipartButNotHaveAnyPart() throws Exception {
        Method method = getMethod("blockingRestWithMultipartButNotHaveAnyPart");
        Exception exp = assertThrows(ZodHttpException.class,
            () -> AnnotationHandler.parseAnnotation(m_factory, method));
        assertEquals(
            "@Part is required when using @Multipart for method RestClientTest.blockingRestWithMultipartButNotHaveAnyPart",
            exp.getMessage());
    }

    @Test
    public void testParseAnnotation_ReturnVoidMethod() throws Exception {

        Method method = getMethod("returnVoidMethod");
        Exception exp = assertThrows(ZodHttpException.class,
            () -> AnnotationHandler.parseAnnotation(m_factory, method));
        assertEquals(
            "Service methods cannot return void for method RestClientTest.returnVoidMethod",
            exp.getMessage());
    }

    @Test
    public void testParseAnnotation_Async() throws Exception {
        Method method = getMethod("nonBlockingMethod");
        HttpServiceMethod serviceMethod = (HttpServiceMethod) AnnotationHandler.parseAnnotation(m_factory, method);

        assertNotNull(serviceMethod);
    }

    @Test
    public void testParseAnnotation_AsyncButNotReturnCompletableFuture() throws Exception {

        Method method = getMethod("nonBlockingButNotReturnCompletableFuture");
        Exception exp = assertThrows(ZodHttpException.class,
            () -> AnnotationHandler.parseAnnotation(m_factory, method));
        assertEquals(
            "Service method must is a ParameterizedType of CompletableFuture for method RestClientTest.nonBlockingButNotReturnCompletableFuture",
            exp.getMessage());
    }

    @Test
    public void testParseAnnotation_AsyncButNotReturnCompletableFuture_2() throws Exception {

        Method method = getMethod("nonBlockingButNotReturnCompletableFuture2");
        Exception exp = assertThrows(ZodHttpException.class,
            () -> AnnotationHandler.parseAnnotation(m_factory, method));
        assertEquals(
            "Service method async must return CompletableFuture for method RestClientTest.nonBlockingButNotReturnCompletableFuture2",
            exp.getMessage());
    }


    private Method getMethod(String name, Class... clazz) throws NoSuchMethodException {
        return RestClientTest.class.getMethod(name, clazz);
    }
}