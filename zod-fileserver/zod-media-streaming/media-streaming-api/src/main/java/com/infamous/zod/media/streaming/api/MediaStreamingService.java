package com.infamous.zod.media.streaming.api;

import java.io.File;

public interface MediaStreamingService {

    MediaStreamingFrame makeFrame(final File asset, String range);
}
