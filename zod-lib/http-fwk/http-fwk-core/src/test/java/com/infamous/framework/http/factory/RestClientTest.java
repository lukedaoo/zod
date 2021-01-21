package com.infamous.framework.http.factory;

import com.infamous.framework.http.Body;
import com.infamous.framework.http.PathParam;
import com.infamous.framework.http.QueryParam;
import com.infamous.framework.http.Url;

interface RestClientTest {

    int testHeader(@com.infamous.framework.http.Header("Dynamic-Header") String dynamicHeader);

    int testPath(@PathParam(value = "fileId", encoded = true) String fileId);

    int testQuery(@QueryParam(value = "id", encoded = true) String id);

    int testUrl(@Url String url);

    int testUrlWithFullUrl(@Url(fullUrl = true) String url);

    int testWithBody(@Body String object);

    // int testWithMultipartBody(@Part)
}
