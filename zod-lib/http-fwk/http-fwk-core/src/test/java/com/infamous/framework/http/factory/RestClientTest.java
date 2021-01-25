package com.infamous.framework.http.factory;

import com.infamous.framework.http.Async;
import com.infamous.framework.http.Body;
import com.infamous.framework.http.Header;
import com.infamous.framework.http.Headers;
import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.MultiPart;
import com.infamous.framework.http.Part;
import com.infamous.framework.http.PathParam;
import com.infamous.framework.http.QueryParam;
import com.infamous.framework.http.Rest;
import com.infamous.framework.http.Url;
import java.util.List;
import java.util.concurrent.CompletableFuture;

interface RestClientTest {

    int testHeader(@com.infamous.framework.http.Header("Dynamic-Header") String dynamicHeader);

    int testPath(@PathParam(value = "fileId", encoded = true) String fileId);

    int testQuery(@QueryParam(value = "id", encoded = true) String id);

    int testUrl(@Url String url);

    int testUrlWithFullUrl(@Url(fullUrl = true) String url);

    int testWithBody(@Body String object);

    int testWithMultipartBody(@Part("files") String file);

    @Headers({
        "Content-Type: application/xml",
        "Static-Header-Name: Static-Header-Value"
    })
    int testStaticHeader();

    @Headers({})
    int testEmptyHeader();

    @Headers({
        "ContentType/application/xml"
    })
    int testHeadersInvalid();

    @Rest(method = HttpMethod.POST)
    int testRest();

    @Headers({
        "Content-Type: application/xml",
        "Static-Header-Name: Static-Header-Value"
    })
    @Rest(method = HttpMethod.POST, url = "/find/{fileId}", contentType = "application/xml")
    String blockingRestWithBody(@Url String url, @Body String body, @PathParam("fileId") String fileId,
                                @QueryParam("group") Integer group,
                                @Header("Dynamic-Header-Name") String dynamicHeaders);

    @Rest(method = HttpMethod.POST, url = "/find", contentType = "application/xml")
    String blockingRestWith2Body(@Body String body1, @Body String body2);

    @MultiPart
    @Rest(method = HttpMethod.POST, url = "/find", contentType = "application/xml")
    String blockingRestWithBodyAndMultiPart(@Body String body1, @Part("name") String name);

    @MultiPart
    @Rest(method = HttpMethod.POST, url = "/find", contentType = "application/xml")
    String blockingRestWithMultipartButNotHaveAnyPart();

    @Rest(method = HttpMethod.POST, url = "/find", contentType = "application/xml")
    void returnVoidMethod();

    @Async
    @Rest(method = HttpMethod.POST, url = "/find", contentType = "application/xml")
    CompletableFuture<String> nonBlockingMethod();

    @Async
    @Rest(method = HttpMethod.POST, url = "/find", contentType = "application/xml")
    String nonBlockingButNotReturnCompletableFuture();

    @Async
    @Rest(method = HttpMethod.POST, url = "/find", contentType = "application/xml")
    List<String> nonBlockingButNotReturnCompletableFuture2();
}
