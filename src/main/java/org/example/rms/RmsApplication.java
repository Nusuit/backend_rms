package org.example.rms;

import org.example.rms.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;


@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})

public class RmsApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RmsApplication.class, args);
    }
}