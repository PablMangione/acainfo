package com.acainfo.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Propiedades de configuración para Rate Limiting.
 * Se mapean desde application.yml con el prefijo 'rate-limiting'
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rate-limiting")
public class RateLimitingProperties {

    private Boolean enabled = false;
    private Integer requestsPerMinute = 60;
    private Integer requestsPerHour = 1000;
    private Integer requestsPerDay = 10000;
    private String cacheType = "caffeine";
    private Long blockDurationSeconds = 60L;
}