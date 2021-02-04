package com.infamous.zod.songmgmt.endpoint;

import com.infamous.zod.base.rest.RestEndPoint;
import java.io.InputStream;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/song-management/v1")
public interface SongManagementEndpointV1 {

    @Path("/upload")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    Response uploadSong(
        @FormDataParam("song-info") FormDataBodyPart songVO,
        @FormDataParam("file") InputStream content,
        @FormDataParam("file") FormDataContentDisposition metadata);

    @Path("/upload/multiple")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    Response uploadSong(
        @FormDataParam("song-infos") FormDataBodyPart songs,
        @FormDataParam("files") List<FormDataBodyPart> bodyParts);

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    Response list();
}
