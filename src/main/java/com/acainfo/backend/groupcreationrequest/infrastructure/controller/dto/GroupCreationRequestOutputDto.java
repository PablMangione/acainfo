package com.acainfo.backend.groupcreationrequest.infrastructure.controller.dto;

import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de salida para representar solicitudes de creación de grupo en las respuestas.
 * Incluye todos los datos de la solicitud incluyendo timestamps y estado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de una solicitud de creación de grupo")
public class GroupCreationRequestOutputDto {

    @Schema(
            description = "Identificador único de la solicitud",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "ID del estudiante que realizó la solicitud",
            example = "1"
    )
    private Long studentId;

    @Schema(
            description = "ID de la asignatura para la cual se solicita el grupo",
            example = "1"
    )
    private Long subjectId;

    @Schema(
            description = "Estado actual de la solicitud",
            example = "PENDING",
            allowableValues = {"PENDING", "APPROVED", "REJECTED", "CANCELLED"}
    )
    private RequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora en que se realizó la solicitud",
            example = "2025-01-15T14:30:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime requestedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora de la última actualización",
            example = "2025-01-15T15:45:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime updatedAt;
}