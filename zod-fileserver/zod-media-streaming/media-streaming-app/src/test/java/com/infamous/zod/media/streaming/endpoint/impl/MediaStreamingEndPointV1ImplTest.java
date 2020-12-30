package com.infamous.zod.media.streaming.endpoint.impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.infamous.zod.media.streaming.controller.MediaStreamingController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaStreamingEndPointV1ImplTest {

    private MediaStreamingController m_controller;
    private MediaStreamingEndPointV1Impl m_mediaStreamingEndPointV1;

    @BeforeEach
    private void setup() {
        m_controller = mock(MediaStreamingController.class);
        m_mediaStreamingEndPointV1 = new MediaStreamingEndPointV1Impl(m_controller);
    }

    @Test
    public void testView() {
        m_mediaStreamingEndPointV1.view("[ROCK]+Hard+Rock+Hallelujah.mp3", "Range");
        verify(m_controller).view(eq("[ROCK] Hard Rock Hallelujah.mp3"), eq("Range"));
    }

}