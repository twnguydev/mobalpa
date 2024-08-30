package com.mobalpa.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Archideco E-commerce API",
        version = "1.0",
        description = "Welcome to the Archideco E-commerce API. This API allows you to manage users, products, orders, and more. Feel free to explore the API and test the endpoints.",
        contact = @Contact(
            name = "Web@cademie Epitech Marseille",
            email = "tanguy.gibrat@epitech.eu",
            url = "https://tanguygibrat.fr"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    )
)
public class SwaggerConfig {
}