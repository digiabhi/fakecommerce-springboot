package com.example.fakecommerce;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
public class FakeCommerceApplication {

    public static void main(String[] args) {
        log.info("Initializing environment configurations from .env...");
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        log.info("Starting FakeCommerceApplication...");
        SpringApplication.run(FakeCommerceApplication.class, args);
        log.info("FakeCommerceApplication started successfully.");
    }

}
