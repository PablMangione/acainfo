package com.acainfo.backend.student.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO específico para el cambio de contraseña de un estudiante.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para actualizar la contraseña de un estudiante")
public class StudentPasswordUpdateDto {

    @NotBlank(message = "La contraseña actual es obligatoria")
    @Schema(
            description = "Contraseña actual del estudiante",
            example = "OldPassword123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String currentPassword;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
    )
    @Schema(
            description = "Nueva contraseña del estudiante",
            example = "NewPassword456",
            minLength = 8,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String newPassword;

    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    @Schema(
            description = "Confirmación de la nueva contraseña",
            example = "NewPassword456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String confirmPassword;
}