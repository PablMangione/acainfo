package com.acainfo.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Propiedades generales de la aplicación.
 * Se mapean desde application.yml con el prefijo 'app'
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private InitData initData = new InitData();
    private Monitoring monitoring = new Monitoring();
    private Notifications notifications = new Notifications();
    private ScheduledTasks scheduledTasks = new ScheduledTasks();

    @Data
    public static class InitData {
        private Boolean enabled = false;
        private Boolean createAdmin = false;
        private String adminEmail = "admin@acainfo.dev";
        private String adminPassword = "Admin123Dev";
        private String adminName = "Administrador Sistema";
    }

    @Data
    public static class Monitoring {
        private Boolean enabled = false;
        private Integer intervalSeconds = 60;
    }

    @Data
    public static class Notifications {
        private Boolean enabled = false;
        private String fromEmail = "noreply@acainfo.com";
    }

    @Data
    public static class ScheduledTasks {
        private Boolean enabled = false;
        private String cronExpression = "0 0 * * * *";
    }
}