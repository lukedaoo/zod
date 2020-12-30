package com.infamous.zod.media.streaming.endpoint.impl;

import com.infamous.zod.base.rest.RestEndPoint;
import com.infamous.zod.media.streaming.controller.MediaStreamingController;
import com.infamous.zod.media.streaming.endpoint.MediaStreamingEndPointV1;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/view/v1")
public class MediaStreamingEndPointV1Impl implements MediaStreamingEndPointV1 {

    private final MediaStreamingController m_controller;

    public MediaStreamingEndPointV1Impl(MediaStreamingController controller) {
        m_controller = controller;
    }

    @Path("/{fileId}")
    @GET
    @Produces("audio/mp3")
    @RestEndPoint
    public Response view(@PathParam("fileId") String fileId, @HeaderParam("Range") String range) {
        String newFileId = URLDecoder.decode(fileId, StandardCharsets.UTF_8);
        return m_controller.view(newFileId, range);
    }
}