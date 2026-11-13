package com.mityaev.fitnessai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Имя нашей схемы безопасности (может быть любым)
        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()
                // 1. Говорим Сваггеру: "Применяй этот замочек ко всем запросам по умолчанию"
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 2. Описываем сам замочек: он должен передавать Bearer Token в заголовке
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}