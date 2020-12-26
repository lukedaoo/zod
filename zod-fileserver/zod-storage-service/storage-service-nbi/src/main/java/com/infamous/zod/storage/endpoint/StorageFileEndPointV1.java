package com.infamous.zod.storage.endpoint;

import com.infamous.zod.base.rest.BaseEndPoint;
import com.infamous.zod.base.rest.RestEndPoint;
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
public interface StorageFileEndPointV1 extends BaseEndPoint {

    @GET
    @Path("/download/{id}")
    @RestEndPoint
    Response download(@PathParam("id") String id);

    @GET
    @Path("/download/multiple")
    @RestEndPoint
    Response multipleDownload(@Context HttpServletRequest request, @QueryParam("id") List<String> ids);

    @Path("/upload")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    Response uploadFile(@FormDataParam("file") InputStream content,
                        @FormDataParam("file") FormDataContentDisposition metadata,
                        @HeaderParam("Content-Length") long len);

    @Path("/info/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    Response info(@PathParam("id") String id);

    @Path("/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    Response info();

    @Path("/upload/multiple")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    Response uploadFile(@FormDataParam("files") List<FormDataBodyPart> bodyParts);
}
