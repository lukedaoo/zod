package com.infamous.zod.storage.controller.impl;

import com.infamous.zod.storage.controller.StorageFileController;
import com.infamous.zod.storage.repository.StorageFileRepository;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StorageFileControllerImpl implements StorageFileController {

    private StorageFileRepository m_repository;

    @Autowired
    public StorageFileControllerImpl(StorageFileRepository repository) {
        m_repository = repository;
    }

    @Override
    public Response download(String id) throws IOException {
        return null;
    }

    @Override
    public Response multipleDownload(HttpServletRequest request, List<String> ids) throws FileNotFoundException {
        return null;
    }

    @Override
    public Response uploadFile(InputStream content, FormDataContentDisposition metadata) {
        return null;
    }

    @Override
    public Response info(String id) throws FileNotFoundException {
        return null;
    }

    @Override
    public Response uploadFile(List<FormDataBodyPart> bodyParts) {
        return null;
    }

    @Override
    public Response getAll() {
        return Response.ok(m_repository.findAll()).build();
    }
}
