package com.infamous.zod.base.rest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JerseyRestConfigTest {


    @Test
    public void testJerseyRestConfig() {
        JerseyRestConfig config = new JerseyRestConfig((resource) -> {
            resource.register(NoOptsController.class);
        });

        assertTrue(config.isRegistered(NoOptsController.class));
    }
}


class NoOptsController {

}