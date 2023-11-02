package com.fredgar.pe.business.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Fredy Huachaca",
                        email = "fredy.huachaca21@gmail.com",
                        url = "https://fredgar.com.pe"
                ),
                description = "OpenApi documentation for Exchange Rate API",
                title = "OpenApi specification - FREDGAR",
                version = "1.0.0",
                license = @License(
                        name = "Licence name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://fredgar.com.pe"
                )
        }
)
public class OpenApiConfig {
}
