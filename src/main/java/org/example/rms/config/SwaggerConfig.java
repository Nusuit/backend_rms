package org.example.rms.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenApiCustomizer disableDefaultResponses() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> {
            pathItem.readOperations().forEach(operation -> {
                // Xóa tất cả responses mặc định
//                operation.getResponses().clear();

                // Hoặc chỉ giữ lại những responses bạn muốn
                // operation.getResponses().keySet().removeIf(key -> !key.equals("200"));
            });
        });
    }


    
}
