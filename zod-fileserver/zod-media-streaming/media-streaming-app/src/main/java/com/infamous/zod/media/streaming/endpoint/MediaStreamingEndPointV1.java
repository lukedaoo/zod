package com.infamous.zod.media.streaming.endpoint;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.base.rest.BaseEndPoint;
import com.infamous.zod.media.streaming.controller.MediaStreamingController;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/view/v1")
public class MediaStreamingEndPointV1 extends BaseEndPoint {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(MediaStreamingEndPointV1.class, "media.streaming");

    private final MediaStreamingController m_controller;

    @Autowired
    public MediaStreamingEndPointV1(MediaStreamingController controller) {
        m_controller = controller;
        postInitLog(() -> "Register MediaStreamingEndPoint - /view/v1");
    }


    @Path("/{fileId}")
    @GET
    @Produces("audio/mp3")
    public Response view(@PathParam("fileId") String fileId, @HeaderParam("Range") String range) {
        return executeWithTemplate(() -> {
            String newFileId = URLDecoder.decode(fileId, StandardCharsets.UTF_8);
            return m_controller.view(newFileId, range);
        });
    }

    @Override
    protected ZodLogger getLogger() {
        return LOGGER;
    }
}
