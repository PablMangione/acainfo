package com.acainfo.backend.auth.infrastructure.controller.dto;

import com.acainfo.backend.auth.domain.model.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO para la respuesta de login exitoso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta tras un login exitoso")
public class LoginResponseDto {

    @Schema(
            description = "Token de acceso JWT",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String accessToken;

    @Schema(
            description = "Token de refresco JWT",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String refreshToken;

    @Schema(
            description = "Tipo de token",
            example = "Bearer"
    )
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(
            description = "Tiempo de expiración del access token en segundos",
            example = "900"
    )
    private Long expiresIn;

    @Schema(
            description = "Información del usuario autenticado"
    )
    private UserInfoDto user;

    /**
     * DTO anidado con la información del usuario
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Información básica del usuario autenticado")
    public static class UserInfoDto {

        @Schema(description = "ID del usuario", example = "STUDENT_123")
        private String id;

        @Schema(description = "Email del usuario", example = "usuario@ejemplo.com")
        private String email;

        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
        private String name;

        @Schema(description = "Tipo de usuario", example = "STUDENT")
        private UserType userType;

        @Schema(description = "Roles del usuario", example = "[\"ROLE_STUDENT\"]")
        private Set<String> roles;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(
                description = "Fecha y hora del login",
                example = "2024-01-15T10:30:00",
                type = "string",
                format = "date-time"
        )
        private LocalDateTime loginAt;
    }
}