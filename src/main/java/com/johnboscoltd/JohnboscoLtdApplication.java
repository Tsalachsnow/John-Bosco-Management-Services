package com.johnboscoltd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JohnboscoLtdApplication {

    public static void main(String[] args) {
        SpringApplication.run(JohnboscoLtdApplication.class, args);
    }

}
