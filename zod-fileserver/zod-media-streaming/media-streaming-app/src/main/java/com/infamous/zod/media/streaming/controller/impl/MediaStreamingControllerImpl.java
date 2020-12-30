package com.infamous.zod.media.streaming.controller.impl;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.media.streaming.api.MediaStreamingFrame;
import com.infamous.zod.media.streaming.api.MediaStreamingService;
import com.infamous.zod.media.streaming.controller.MediaStreamingController;
import com.infamous.zod.media.streaming.service.MediaStreamingServiceImpl;
import com.infamous.zod.storage.repository.StorageFileRepository;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaStreamingControllerImpl implements MediaStreamingController {

    private static final ZodLogger LOGGER = ZodLoggerUtil
        .getLogger(MediaStreamingServiceImpl.class, "media.streaming");

    private final StorageFileRepository m_fileRepository;
    private final MediaStreamingService m_mediaStreamingService;

    @Autowired
    public MediaStreamingControllerImpl(StorageFileRepository repository, MediaStreamingService mediaStreamingService) {
        this.m_fileRepository = repository;
        this.m_mediaStreamingService = mediaStreamingService;
    }

    @Override
    public Response view(String fileId, String range) {
        File asset = m_fileRepository.findPhysicalFile(fileId);

        if (range == null) {
            StreamingOutput streamer = output -> {
                try (FileChannel inputChannel = new FileInputStream(asset).getChannel();
                    WritableByteChannel outputChannel = Channels.newChannel(output)) {
                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                }
            };
            return Response
                .ok(streamer)
                .status(Response.Status.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_LENGTH, asset.length())
                .build();
        }

        MediaStreamingFrame frame = m_mediaStreamingService.makeFrame(asset, range);

        String contentRange = frame.getRange().generateRangeString();
        Response.ResponseBuilder res = Response.ok(frame.getOutput())
            .status(Response.Status.PARTIAL_CONTENT)
            .header("Accept-Ranges", "bytes")
            .header("Content-Range", contentRange)
            .header(HttpHeaders.CONTENT_LENGTH, frame.getOutput().getLength())
            .header(HttpHeaders.LAST_MODIFIED, new Date(asset.lastModified()));
        LOGGER.debug("Streaming file [" + asset.getName() + "]: " + contentRange);
        return res.build();
    }
}
