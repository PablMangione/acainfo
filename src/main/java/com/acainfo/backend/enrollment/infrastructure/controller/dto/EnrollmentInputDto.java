package com.acainfo.backend.enrollment.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO de entrada para la creación de inscripciones.
 * Contiene las validaciones de formato para los datos de entrada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear una nueva inscripción")
public class EnrollmentInputDto {

    @NotNull(message = "El ID del estudiante es obligatorio")
    @Schema(
            description = "ID del estudiante que se inscribe",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long studentId;

    @NotNull(message = "El ID del grupo es obligatorio")
    @Schema(
            description = "ID del grupo al que se inscribe el estudiante",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long groupId;
}