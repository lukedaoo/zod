package com.infamous.zod.media.streaming.endpoint;

import com.infamous.zod.base.rest.RestEndPoint;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/view/v1")
public interface MediaStreamingEndPointV1 {


    @Path("/{fileId}")
    @GET
    @Produces("audio/mp3")
    @RestEndPoint
    Response view(@PathParam("fileId") String fileId, @HeaderParam("Range") String range);
}
