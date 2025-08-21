package com.acainfo.backend.teacher.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO de entrada para la actualización de profesores.
 * Similar a TeacherInputDto pero sin incluir la contraseña ni el estado admin.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para actualizar un profesor existente")
public class TeacherEditInputDto {

    @NotBlank(message = "El nombre del profesor es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(
            description = "Nombre completo del profesor",
            example = "Juan Pérez García",
            minLength = 2,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 150, message = "El email no puede exceder los 150 caracteres")
    @Schema(
            description = "Correo electrónico del profesor",
            example = "juan.perez@universidad.edu",
            maxLength = 150,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Pattern(
            regexp = "^(\\+34)?[6789]\\d{8}$",
            message = "El número de teléfono debe ser válido (formato español)"
    )
    @Schema(
            description = "Número de teléfono del profesor",
            example = "+34612345678",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String phoneNumber;
}