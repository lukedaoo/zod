package com.infamous.zod.storage.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface StorageFileController {

    Response download(String id) throws IOException;

    Response multipleDownload(HttpServletRequest request, List<String> ids) throws FileNotFoundException;

    Response uploadFile(InputStream content, FormDataContentDisposition metadata);

    Response info(String id) throws FileNotFoundException;

    Response uploadFile(List<FormDataBodyPart> bodyParts);

    Response getAll();
}