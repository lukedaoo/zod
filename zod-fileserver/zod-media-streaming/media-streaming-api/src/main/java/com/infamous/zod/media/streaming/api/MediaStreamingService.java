package com.infamous.zod.media.streaming.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface MediaStreamingService {

    MediaStreamingFrame makeFrame(final File asset, String range);
}
