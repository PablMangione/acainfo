package com.acainfo.backend.auth.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API.
 * Define la información general, esquemas de seguridad y servidores.
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:AcaInfo}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        // Definir el esquema de seguridad JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Ingrese el token JWT sin el prefijo 'Bearer'");

        // Definir el requisito de seguridad
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers())
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }

    /**
     * Información general de la API
     */
    private Info apiInfo() {
        return new Info()
                .title(applicationName + " API")
                .description("Sistema de Gestión Académica - API REST para la gestión de clases particulares")
                .version("1.0.0")
                .contact(apiContact())
                .license(apiLicense())
                .termsOfService("https://acainfo.com/terms");
    }

    /**
     * Información de contacto
     */
    private Contact apiContact() {
        return new Contact()
                .name("Equipo de Desarrollo AcaInfo")
                .email("dev@acainfo.com")
                .url("https://acainfo.com");
    }

    /**
     * Información de licencia
     */
    private License apiLicense() {
        return new License()
                .name("Licencia Privada")
                .url("https://acainfo.com/license");
    }

    /**
     * Servidores disponibles
     */
    private List<Server> apiServers() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor de desarrollo local");

        Server devServer = new Server()
                .url("https://api-dev.acainfo.com")
                .description("Servidor de desarrollo");

        Server prodServer = new Server()
                .url("https://api.acainfo.com")
                .description("Servidor de producción");

        return List.of(localServer, devServer, prodServer);
    }
}