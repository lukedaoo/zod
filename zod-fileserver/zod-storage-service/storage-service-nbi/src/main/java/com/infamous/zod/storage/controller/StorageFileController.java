package com.infamous.zod.storage.controller;

import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface StorageFileController {

    Response download(String id);

    Response multipleDownload(HttpServletRequest request, List<String> ids);

    Response uploadFile(InputStream content, FormDataContentDisposition metadata);

    Response info(String id);

    Response uploadFile(List<FormDataBodyPart> bodyParts);

    Response getAll();
}