package org.example.rms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class RmsApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RmsApplication.class, args);

    }
}