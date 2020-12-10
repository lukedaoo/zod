package com.infamous.zod.storage;

import com.infamous.zod.base.jpa.ZodJpaAutoConfig;
import com.infamous.zod.ftp.server.EnableZodFTPServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ZodJpaAutoConfig
@EnableZodFTPServer
public class StorageApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StorageApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
