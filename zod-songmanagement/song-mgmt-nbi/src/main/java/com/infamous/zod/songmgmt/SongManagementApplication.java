package com.infamous.zod.songmgmt;

import com.infamous.zod.base.common.ZodCommonServiceAutoConfig;
import com.infamous.zod.base.jpa.ZodJpaAutoConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ZodJpaAutoConfig
@ZodCommonServiceAutoConfig
@ComponentScan(basePackages = "com.infamous.zod.songmgmt")
public class SongManagementApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(SongManagementApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

    }
}
