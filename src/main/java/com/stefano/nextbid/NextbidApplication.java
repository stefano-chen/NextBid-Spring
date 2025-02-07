package com.stefano.nextbid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class NextbidApplication {

    public static void main(String[] args) {
        SpringApplication.run(NextbidApplication.class, args);
    }

}
