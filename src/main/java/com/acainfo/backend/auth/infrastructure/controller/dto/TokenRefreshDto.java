package com.acainfo.backend.auth.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO para la petición de refresco de token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para refrescar el token de acceso")
public class TokenRefreshDto {

    @NotBlank(message = "El refresh token es obligatorio")
    @Schema(
            description = "Token de refresco JWT",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String refreshToken;
}