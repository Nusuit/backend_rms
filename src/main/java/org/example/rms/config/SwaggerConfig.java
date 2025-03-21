package org.example.rms.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components()
//                        // Định nghĩa JWT Bearer Security Scheme
//                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT"))
//                        .addParameters("Authorization", new Parameter()
//                                .in("header")
//                                .name("Authorization")
//                                .schema(new StringSchema())
//                                .description("JWT Bearer Token")
//                                .required(true))
//                )
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
//    }
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components().addSecuritySchemes("basicScheme", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"))
//                        .addParameters("myHeader1", new Parameter().in("header").schema(new StringSchema()).name("myHeader1")).addHeaders("myHeader2", new Header().description("myHeader2 header").schema(new StringSchema())))
//                .info(new Info()
//                        .title("Petstore API")
//                        .description("This is a sample server Petstore server. You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/). For this sample, you can use the api key `special-key` to test the authorization filters.")
//                        .termsOfService("http://swagger.io/terms/")
//                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
//    }
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info().title("My API").version("1.0").description("Muong cha la"));
//    }
//    @Bean
//    public OpenApiCustomizer customHeaderOpenApiCustomizer() {
//        return new OpenApiCustomizer() {
//            @Override
//            public void customise(OpenAPI openApi) {
//                HeaderParameter customHeader = (HeaderParameter) new HeaderParameter()
//                        .name("X-Custom-Header")
//                        .description("This is a custom header")
//                        .required(true)
//                        .example("Example Value");
//
//                for (PathItem pathItem : openApi.getPaths().values()) {
//                    for (Operation operation : pathItem.readOperations()) {
//                        operation.addParametersItem(customHeader);
//                    }
//                }
//            }
//        };
//    }


    
}
