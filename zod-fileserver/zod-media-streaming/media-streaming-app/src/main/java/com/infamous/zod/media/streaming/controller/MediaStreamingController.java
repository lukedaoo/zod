package com.infamous.zod.media.streaming.controller;

import javax.ws.rs.core.Response;

public interface MediaStreamingController {

    Response view(String fileId, String range);
}
