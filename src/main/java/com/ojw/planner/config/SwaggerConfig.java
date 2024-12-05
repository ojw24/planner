package com.ojw.planner.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("0. All-API")
                .packagesToScan("com.ojw.planner.app")
                .addOpenApiCustomizer(getSecurityCustomizer())
                .build();
    }

    private OpenApiCustomizer getSecurityCustomizer() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .scheme("bearer")
                .bearerFormat("JWT")
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER);

        return OpenApi -> OpenApi
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .getComponents().addSecuritySchemes("Authorization", securityScheme);
    }

}
