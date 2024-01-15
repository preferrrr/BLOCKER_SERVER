package com.blocker.blocker_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BlockerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockerServerApplication.class, args);
    }

}
