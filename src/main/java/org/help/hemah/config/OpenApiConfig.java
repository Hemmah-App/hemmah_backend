package org.help.hemah.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI configureOpenAPI() {
        return new OpenAPI().info(new Info().title("Hemah API").version("1.0.0").description("Hemah API Documentation"));
    }
}
