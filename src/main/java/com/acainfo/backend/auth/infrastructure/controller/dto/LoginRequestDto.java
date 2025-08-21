package com.acainfo.backend.auth.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO para la petición de login
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para iniciar sesión")
public class LoginRequestDto {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Schema(
            description = "Email del usuario",
            example = "usuario@ejemplo.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(
            description = "Contraseña del usuario",
            example = "Password123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}