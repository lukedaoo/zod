package com.infamous.framework.http.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.ZodHttpException;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.framework.http.core.BodyPartFactory;
import com.infamous.framework.http.core.HttpRequest;
import com.infamous.framework.http.core.RequestBody;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class JavaHttpMapperTest {

    @Test
    public void testMapEntity_WithRequestHasNoBody() {
        java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest
            .newBuilder(URI.create("http://localhost:8080"));

        HttpRequest request = mock(com.infamous.framework.http.core.HttpRequest.class);

        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getBody()).thenReturn(Optional.empty());

        builder = JavaHttpMapper.mapBody(builder, request);

        assertNotNull(builder);
        assertEquals(0, builder.build().bodyPublisher().get().contentLength());
    }

    @Test
    public void testMapEntity_WithRequestHasBody() {

        testBodyEntityWith("json-content", () -> "json-content".getBytes().length);
    }

    @Test
    public void testMapEntity_WithRequestHasBody_ButBodyIsEmpty() {
        testBodyEntityWith(null, () -> 0, requestBody -> when(requestBody.isEmpty()).thenReturn(true));

    }

    @Test
    public void testMapEntity_WithRequestHasBody_AndBodyIsInputStream() throws FileNotFoundException {
        File file = new File("src/test/resources/test.txt");
        testBodyEntityWith(new FileInputStream(file), () -> -1);
    }

    @Test
    public void testMapEntity_WithRequestHasBody_AndBodyIsFile() throws FileNotFoundException {

        File file = new File("src/test/resources/test.txt");
        testBodyEntityWith(file, file::length);
    }

    @Test
    public void testMapEntity_WithRequestHasBody_AndBodyIsByteArr() {
        byte[] bytes = new byte[1];
        bytes[0] = 1;
        testBodyEntityWith(bytes, () -> bytes.length);
    }

    @Test
    public void testMap_Entity_WithRequestHasBody_AndBodyIsMultipart() throws FileNotFoundException {
        File file = new File("src/test/resources/test.txt");

        java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest
            .newBuilder(URI.create("http://localhost:8080"));

        HttpRequest request = mock(com.infamous.framework.http.core.HttpRequest.class);
        when(request.getMethod()).thenReturn(HttpMethod.GET);

        BodyPart b1 = BodyPartFactory.part("name", "infamouSs", "text/plain");
        BodyPart b2 = BodyPartFactory.part("email", "abc@gmail.com", "text/email");
        BodyPart b3 = BodyPartFactory.part("file1", file, null);
        BodyPart b4 = BodyPartFactory
            .part("file2", new FileInputStream(file), "application/octet-stream", "fileName.txt");

        BodyPart b5 = BodyPartFactory.part("file3", new byte[1], "application/octet-stream", "newFile.txt");
        List<BodyPart> bodyPartList = Arrays.asList(b1, b2, b3, b4, b5);

        RequestBody requestBody = mock(RequestBody.class);
        when(requestBody.isEmpty()).thenReturn(false);
        when(requestBody.isBodyEntity()).thenReturn(false);
        when(requestBody.isMultiPart()).thenReturn(true);
        when(requestBody.multiParts()).thenReturn(bodyPartList);
        when(requestBody.getCharset()).thenReturn(StandardCharsets.UTF_8);
        Optional<RequestBody> requestBodyOpt = Optional.of(requestBody);
        when(request.getBody()).thenReturn(requestBodyOpt);

        java.net.http.HttpRequest.Builder res = JavaHttpMapper.mapBody(builder, request);

        assertNotNull(res);
        assertTrue(res.build().bodyPublisher().get().contentLength() > 0);
    }

    @Test
    public void testMapEntity_ThrowEx() {
        File file = new File("src/test/resources/not_found.txt");

        Exception exp = assertThrows(ZodHttpException.class, () -> testBodyEntityWith(file, () -> file.length()));
        assertEquals("java.io.FileNotFoundException: "
            + "src/test/resources/not_found.txt not found", exp.getMessage());
    }

    private RequestBody mockBodyEntity(BodyPart bodyEntity) {
        RequestBody requestBody = mock(RequestBody.class);

        when(requestBody.isEmpty()).thenReturn(false);
        when(requestBody.isBodyEntity()).thenReturn(true);
        when(requestBody.isMultiPart()).thenReturn(false);
        when(requestBody.entity()).thenReturn(bodyEntity);
        when(requestBody.getCharset()).thenReturn(StandardCharsets.UTF_8);

        return requestBody;
    }


    private BodyPart createBody(Object object) {
        return BodyPartFactory.body(object);
    }


    private void testBodyEntityWith(Object value, Supplier<?> objectSupp) {
        testBodyEntityWith(value, objectSupp, requestBody -> {
        });
    }

    private void testBodyEntityWith(Object value, Supplier<?> objectSupp, Consumer<RequestBody> requestBodyConsumer) {
        java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest
            .newBuilder(URI.create("http://localhost:8080"));

        HttpRequest request = mock(com.infamous.framework.http.core.HttpRequest.class);

        when(request.getMethod()).thenReturn(HttpMethod.GET);
        RequestBody requestBody = mockBodyEntity(createBody(value));
        requestBodyConsumer.accept(requestBody);

        Optional<RequestBody> requestBodyOpt = Optional.of(requestBody);
        when(request.getBody()).thenReturn(requestBodyOpt);

        java.net.http.HttpRequest.Builder res = JavaHttpMapper.mapBody(builder, request);
        assertNotNull(res);
        assertEquals(String.valueOf(objectSupp.get()),
            String.valueOf(res.build().bodyPublisher().get().contentLength()));
    }

}