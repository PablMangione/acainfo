package com.acainfo.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Propiedades adicionales de seguridad.
 * Se mapean desde application.yml con el prefijo 'security'
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private Boolean requireSsl = false;
    private Headers headers = new Headers();

    @Data
    public static class Headers {
        private String contentSecurityPolicy = "default-src 'self'";
        private String frameOptions = "DENY";
        private String xssProtection = "1; mode=block";
        private String contentTypeOptions = "nosniff";
    }
}