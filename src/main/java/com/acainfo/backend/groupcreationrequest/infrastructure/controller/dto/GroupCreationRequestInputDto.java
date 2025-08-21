package com.acainfo.backend.groupcreationrequest.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO de entrada para la creación de solicitudes de grupo.
 * Contiene las validaciones de formato para los datos de entrada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear una nueva solicitud de grupo")
public class GroupCreationRequestInputDto {

    @NotNull(message = "El ID del estudiante es obligatorio")
    @Schema(
            description = "ID del estudiante que realiza la solicitud",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long studentId;

    @NotNull(message = "El ID de la asignatura es obligatorio")
    @Schema(
            description = "ID de la asignatura para la cual se solicita crear un grupo",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long subjectId;
}