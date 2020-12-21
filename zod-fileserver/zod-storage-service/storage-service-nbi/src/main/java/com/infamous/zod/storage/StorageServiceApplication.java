package com.infamous.zod.storage;

import com.infamous.zod.base.common.ZodCommonServiceAutoConfig;
import com.infamous.zod.base.jpa.ZodJpaAutoConfig;
import com.infamous.zod.base.rest.JerseyRestConfig;
import com.infamous.zod.ftp.server.EnableZodFTPServer;
import com.infamous.zod.media.streaming.endpoint.MediaStreamingEndPointV1;
import com.infamous.zod.storage.endpoint.StorageFileEndPointV1;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public JerseyRestConfig restConfig() {
        return new JerseyRestConfig((config) -> {
            config.register(StorageFileEndPointV1.class);
            config.register(MediaStreamingEndPointV1.class);
        });
    }

    @Override
    public void run(String... args) throws Exception {
    }
}