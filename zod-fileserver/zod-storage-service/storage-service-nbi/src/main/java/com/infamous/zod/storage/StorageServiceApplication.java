package com.infamous.zod.storage;

import com.infamous.zod.base.common.ZodCommonServiceAutoConfig;
import com.infamous.zod.base.jpa.ZodJpaAutoConfig;
import com.infamous.zod.ftp.server.EnableZodFTPServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ZodCommonServiceAutoConfig
@ZodJpaAutoConfig
@EnableZodFTPServer
@ComponentScan(basePackages = "com.infamous.zod.storage, com.infamous.zod.media.streaming")
public class StorageServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StorageServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}