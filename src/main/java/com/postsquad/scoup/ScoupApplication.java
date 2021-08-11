package com.postsquad.scoup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ScoupApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoupApplication.class, args);
    }

}
