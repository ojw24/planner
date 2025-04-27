package com.ojw.planner.config;

import io.swagger.v3.oas.models.info.Info;
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
                .group("0. All API")
                .packagesToScan("com.ojw.planner.app")
                .addOpenApiCustomizer(getSecurityCustomizer("All"))
                .build();
    }

    @Bean
    public GroupedOpenApi plannerApi() {
        return GroupedOpenApi.builder()
                .group("1. Planner API")
                .packagesToScan("com.ojw.planner.app.planner")
                .addOpenApiCustomizer(getSecurityCustomizer("Planner"))
                .build();
    }

    @Bean
    public GroupedOpenApi communityApi() {
        return GroupedOpenApi.builder()
                .group("2. Community API")
                .packagesToScan("com.ojw.planner.app.community")
                .addOpenApiCustomizer(getSecurityCustomizer("Community"))
                .build();
    }

    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("3. System API")
                .packagesToScan("com.ojw.planner.app.system")
                .addOpenApiCustomizer(getSecurityCustomizer("System"))
                .build();
    }

    private OpenApiCustomizer getSecurityCustomizer(String api) {
        SecurityScheme securityScheme = new SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .scheme("bearer")
                .bearerFormat("JWT")
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER);

        return OpenApi -> OpenApi
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .info(apiInfo(api))
                .getComponents().addSecuritySchemes("Authorization", securityScheme);
    }

    private Info apiInfo(String api) {
        return new Info()
                .title("Planner - " + api + " API definition")
                .description(
                        api + " API definition and test UI"
                                + "<br />"
                                + "(Do Test the API by referring to the Schema(DTO) definition below)"
                );
    }

}
