package com.acainfo.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.acainfo.backend.config.properties.*;

/**
 * Configuración principal para habilitar todas las clases de propiedades personalizadas.
 * Esto permite que Spring Boot reconozca y mapee las propiedades del archivo YAML.
 */
@Configuration
@EnableConfigurationProperties({
        AppProperties.class,
        CorsProperties.class,
        RateLimitingProperties.class,
        SecurityProperties.class
})
public class PropertiesConfig {
    // Esta clase solo sirve para habilitar las propiedades
    // No necesita contenido adicional
}