package com.infamous.zod.eureka.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class ZodEurekaServer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ZodEurekaServer.class);
    }

    @Override
    public void run(String... args) {

    }
}
