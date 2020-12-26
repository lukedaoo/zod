package com.infamous.zod.storage.endpoint.impl;

import com.infamous.zod.base.rest.RestEndPoint;
import com.infamous.zod.storage.controller.StorageFileController;
import com.infamous.zod.storage.endpoint.StorageFileEndPointV1;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/storage/v1")
public class StorageFileEndPointV1Impl implements StorageFileEndPointV1 {

    private StorageFileController m_controller;

    public StorageFileEndPointV1Impl(StorageFileController controller) {
        this.m_controller = controller;
    }

    @GET
    @Path("/download/{id}")
    @RestEndPoint
    public Response download(@PathParam("id") String id) {
        return m_controller.download(id);
    }

    @GET
    @Path("/download/multiple")
    @RestEndPoint
    public Response multipleDownload(@Context HttpServletRequest request, @QueryParam("id") List<String> ids) {
        return m_controller.multipleDownload(request, ids);
    }

    @Path("/upload")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    public Response uploadFile(@FormDataParam("file") InputStream content,
                               @FormDataParam("file") FormDataContentDisposition metadata,
                               @HeaderParam("Content-Length") long len) {
        return m_controller.uploadFile(content, metadata);
    }

    @Path("/info/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    public Response info(@PathParam("id") String id) {
        return m_controller.info(id);
    }

    @Path("/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    public Response info() {
        return m_controller.getAll();
    }

    @Path("/upload/multiple")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    public Response uploadFile(@FormDataParam("files") List<FormDataBodyPart> bodyParts) {
        return m_controller.uploadFile(bodyParts);
    }
}
