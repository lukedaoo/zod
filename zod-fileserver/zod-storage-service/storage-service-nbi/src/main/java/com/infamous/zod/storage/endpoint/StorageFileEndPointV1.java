package com.infamous.zod.storage.endpoint;

import com.infamous.zod.storage.controller.StorageFileController;
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
import org.springframework.stereotype.Controller;

@Controller
@Path("/storage/v1")
public class StorageFileEndPointV1 {

    private StorageFileController m_controller;

    public StorageFileEndPointV1(StorageFileController controller) {
        this.m_controller = controller;
    }

    @GET
    @Path("/download/{id}")
    public Response download(@PathParam("id") String id) {
        try {
            return m_controller.download(id);
        } catch (Exception e) {
            return unknownResponse(e);
        }
    }

    @GET
    @Path("/download/multiple")
    public Response multipleDownload(@Context HttpServletRequest request, @QueryParam("id") List<String> ids) {
        try {
            return m_controller.multipleDownload(request, ids);
        } catch (Exception e) {
            return unknownResponse(e);
        }
    }


    @Path("/upload")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@FormDataParam("file") InputStream content,
                               @FormDataParam("file") FormDataContentDisposition metadata,
                               @HeaderParam("Content-Length") long len) {
        try {
            return m_controller.uploadFile(content, metadata);
        } catch (Exception e) {
            return unknownResponse(e);
        }
    }

    @Path("/info/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response info(@PathParam("id") String id) {
        try {
            return m_controller.info(id);
        } catch (Exception e) {
            return unknownResponse(e);
        }
    }

    @Path("/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response info() {
        try {
            return m_controller.getAll();
        } catch (Exception e) {
            return unknownResponse(e);
        }
    }

    @Path("/upload/multiple")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@FormDataParam("files") List<FormDataBodyPart> bodyParts) {
        try {
            return m_controller.uploadFile(bodyParts);
        } catch (Exception e) {
            return unknownResponse(e);
        }
    }

    private Response unknownResponse(Exception e) {
        return Response.status(500).build();
    }
}
