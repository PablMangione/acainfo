package com.acainfo.backend.teacher.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO específico para el cambio de contraseña de un profesor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para actualizar la contraseña de un profesor")
public class TeacherPasswordUpdateDto {

    @NotBlank(message = "La contraseña actual es obligatoria")
    @Schema(
            description = "Contraseña actual del profesor",
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
            description = "Nueva contraseña del profesor",
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