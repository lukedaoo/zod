package com.infamous.framework.logging.appender;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Demo {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(Demo.class, "demo");

    public static void main(String[] args) {

        LOGGER.info("ok bro", new RuntimeException("Exception",
            new IOException("io exception", new FileNotFoundException("file not found"))));
    }
}
