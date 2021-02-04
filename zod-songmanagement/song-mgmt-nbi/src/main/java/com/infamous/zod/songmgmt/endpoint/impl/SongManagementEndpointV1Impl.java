package com.infamous.zod.songmgmt.endpoint.impl;

import com.infamous.zod.base.rest.BaseEndPoint;
import com.infamous.zod.base.rest.RestEndPoint;
import com.infamous.zod.songmgmt.controller.SongManagementController;
import com.infamous.zod.songmgmt.endpoint.SongManagementEndpointV1;
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
public class SongManagementEndpointV1Impl implements BaseEndPoint, SongManagementEndpointV1 {

    private SongManagementController m_controller;

    public SongManagementEndpointV1Impl(SongManagementController controller) {
        m_controller = controller;
    }

    @Path("/upload")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    @Override
    public Response uploadSong(
        @FormDataParam("song-info") FormDataBodyPart songPartAsJson,
        @FormDataParam("file") InputStream content,
        @FormDataParam("file") FormDataContentDisposition metadata) {
        return m_controller.upload(songPartAsJson, content, metadata);
    }

    @Override
    public Response uploadSong(FormDataBodyPart songs, List<FormDataBodyPart> bodyParts) {
        return m_controller.multipleUpload(songs, bodyParts);
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RestEndPoint
    @Override
    public Response list() {
        return m_controller.getAll();
    }
}
