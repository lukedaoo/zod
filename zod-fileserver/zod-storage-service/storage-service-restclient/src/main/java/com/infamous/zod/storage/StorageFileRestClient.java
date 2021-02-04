package com.infamous.zod.storage;

import com.infamous.framework.http.Async;
import com.infamous.framework.http.HttpMethod;
import com.infamous.framework.http.MultiPart;
import com.infamous.framework.http.Part;
import com.infamous.framework.http.PathParam;
import com.infamous.framework.http.QueryParam;
import com.infamous.framework.http.Rest;
import com.infamous.framework.http.RestClient;
import com.infamous.framework.http.core.BodyPart;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.base.rest.entity.UploadResult;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestClient(category = "storageRestClient")
public interface StorageFileRestClient {

    @Async
    @Rest(url = "/download/{id}", method = HttpMethod.GET)
    CompletableFuture<InputStream> download(@PathParam("id") String fileId);

    @Async
    @Rest(url = "/download/multiple", method = HttpMethod.GET)
    CompletableFuture<InputStream> multipleDownload(@QueryParam("id") List<String> ids);

    @Async
    @MultiPart
    @Rest(url = "/upload", method = HttpMethod.POST, contentType = "application/x-www-form-urlencoded")
    CompletableFuture<StorageFileVO> upload(@Part(value = "file") BodyPart<InputStream> bodyPart);

    @Async
    @MultiPart
    @Rest(url = "/upload/multiple", method = HttpMethod.POST, contentType = "application/x-www-form-urlencoded")
    CompletableFuture<UploadResult<StorageFileVO>> upload(@Part(value = "files") List<? extends BodyPart<InputStream>> file);

    @Async
    @Rest(url = "/info/{id}", method = HttpMethod.GET)
    CompletableFuture<StorageFileVO> info(@PathParam("id") String id);

    @Async
    @Rest(url = "/info/", method = HttpMethod.GET)
    CompletableFuture<List<StorageFileVO>> info();
}
