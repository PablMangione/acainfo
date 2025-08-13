package com.acainfo.backend.auth.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * DTO para la respuesta de validación de token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información sobre la validez de un token")
public class TokenValidationResponseDto {

    @Schema(
            description = "Indica si el token es válido",
            example = "true"
    )
    private boolean valid;

    @Schema(
            description = "ID del usuario del token",
            example = "STUDENT_123"
    )
    private String userId;

    @Schema(
            description = "Email del usuario",
            example = "usuario@ejemplo.com"
    )
    private String email;

    @Schema(
            description = "Roles del usuario",
            example = "[\"ROLE_STUDENT\"]"
    )
    private List<String> roles;

    @Schema(
            description = "Tiempo restante hasta expiración en segundos",
            example = "300"
    )
    private Long remainingTime;

    @Schema(
            description = "Mensaje de error si el token no es válido",
            example = "Token expirado"
    )
    private String error;
}