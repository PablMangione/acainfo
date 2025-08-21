package com.acainfo.backend.student.infrastructure.controller.dto;

import com.acainfo.backend.globalenum.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO de entrada para la actualización de estudiantes.
 * Similar a StudentInputDto pero sin incluir la contraseña ni el estado activo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para actualizar un estudiante existente")
public class StudentEditInputDto {

    @NotBlank(message = "El nombre del estudiante es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(
            description = "Nombre del estudiante",
            example = "María",
            minLength = 2,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @NotBlank(message = "El apellido del estudiante es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Schema(
            description = "Apellido del estudiante",
            example = "García López",
            minLength = 2,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 150, message = "El email no puede exceder los 150 caracteres")
    @Schema(
            description = "Correo electrónico del estudiante",
            example = "maria.garcia@estudiante.edu",
            maxLength = 150,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Pattern(
            regexp = "^(\\+34)?[6789]\\d{8}$",
            message = "El número de teléfono debe ser válido (formato español)"
    )
    @Schema(
            description = "Número de teléfono del estudiante",
            example = "+34612345678",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String phoneNumber;

    @NotNull(message = "La carrera es obligatoria")
    @Schema(
            description = "Carrera que cursa el estudiante",
            example = "ING_INF",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"ING_INF", "ING_IND"}
    )
    private Major major;
}