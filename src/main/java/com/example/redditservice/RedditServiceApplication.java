package com.example.redditservice;

import com.example.redditservice.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class RedditServiceApplication {

    // http://localhost:8080/swagger-ui.html#/
    public static void main(String[] args) {
        SpringApplication.run(RedditServiceApplication.class, args);
    }

}
