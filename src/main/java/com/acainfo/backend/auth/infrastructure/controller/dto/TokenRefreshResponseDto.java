package com.acainfo.backend.auth.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para la respuesta de refresco de token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con el nuevo token de acceso")
public class TokenRefreshResponseDto {

    @Schema(
            description = "Nuevo token de acceso JWT",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String accessToken;

    @Schema(
            description = "Tipo de token",
            example = "Bearer"
    )
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(
            description = "Tiempo de expiración del token en segundos",
            example = "900"
    )
    private Long expiresIn;
}