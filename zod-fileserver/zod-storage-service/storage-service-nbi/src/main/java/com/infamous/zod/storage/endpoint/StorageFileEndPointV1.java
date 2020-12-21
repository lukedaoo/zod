package com.infamous.zod.storage.endpoint;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.base.rest.BaseEndPoint;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/storage/v1")
public class StorageFileEndPointV1 extends BaseEndPoint {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(StorageFileEndPointV1.class, "storage.service");

    private StorageFileController m_controller;

    @Autowired
    public StorageFileEndPointV1(StorageFileController controller) {
        this.m_controller = controller;
        postInitLog(() -> "Register StorageFileEndPointV1 - /storage/v1");
    }

    @GET
    @Path("/download/{id}")
    public Response download(@PathParam("id") String id) {
        return executeWithTemplate(() -> m_controller.download(id));
    }

    @GET
    @Path("/download/multiple")
    public Response multipleDownload(@Context HttpServletRequest request, @QueryParam("id") List<String> ids) {
        return executeWithTemplate(() -> m_controller.multipleDownload(request, ids));
    }

    @Path("/upload")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@FormDataParam("file") InputStream content,
                               @FormDataParam("file") FormDataContentDisposition metadata,
                               @HeaderParam("Content-Length") long len) {
        return executeWithTemplate(() -> m_controller.uploadFile(content, metadata));
    }

    @Path("/info/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response info(@PathParam("id") String id) {
        return executeWithTemplate(() -> m_controller.info(id));
    }

    @Path("/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response info() {
        return executeWithTemplate(() -> m_controller.getAll());
    }

    @Path("/upload/multiple")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@FormDataParam("files") List<FormDataBodyPart> bodyParts) {
        return executeWithTemplate(() -> m_controller.uploadFile(bodyParts));
    }

    @Override
    protected ZodLogger getLogger() {
        return LOGGER;
    }
}
