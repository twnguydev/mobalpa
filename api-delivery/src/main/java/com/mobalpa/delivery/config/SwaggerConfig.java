package com.mobalpa.delivery.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Archideco Delivery API",
        version = "1.0",
        description = "Welcome to the Archideco Delivery API. This API permits you to deliver orders, manage parcels, and more. Feel free to explore the API and test the endpoints.",
        contact = @Contact(
            name = "Web@cademie Epitech Marseille",
            email = "tanguy.gibrat@epitech.eu",
            url = "https://tanguygibrat.fr"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    security = {
        @SecurityRequirement(name = "apiKey")
    }
)
@SecurityScheme(
    name = "apiKey",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = "X-API-KEY"
)
public class SwaggerConfig {
}