package com.infamous.zod.media.streaming.service;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.media.streaming.api.MediaRange;
import com.infamous.zod.media.streaming.api.MediaStreamingFrame;
import com.infamous.zod.media.streaming.api.MediaStreamingOutput;
import com.infamous.zod.media.streaming.api.MediaStreamingService;
import java.io.File;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MediaStreamingServiceImpl implements MediaStreamingService {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(MediaStreamingServiceImpl.class, "media.streaming");

    public MediaStreamingServiceImpl() {

    }

    @Override
    public MediaStreamingFrame makeFrame(File asset, String range) {
        Objects.requireNonNull(asset);
        MediaRange mediaRange = MediaRange.of(range, asset.length());
        return createMediaOutput(asset, mediaRange)
            .map(mediaOutput -> MediaStreamingFrame.of(mediaOutput, mediaRange))
            .orElseThrow(() -> new IllegalStateException("Empty frame is created"));
    }

    private Optional<MediaStreamingOutput> createMediaOutput(File asset, MediaRange mediaRange) {
        try {
            MediaStreamingOutput output =
                new MediaStreamingOutput(mediaRange.calculateLength(), mediaRange.getFrom(), asset);
            return Optional.of(output);
        } catch (Exception e) {
            LOGGER.error("Error while creating media output. Asset: " + asset.getName() + ". Range:" + mediaRange, e);
            return Optional.empty();
        }
    }

}
