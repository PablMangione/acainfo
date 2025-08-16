package com.acainfo.backend.enrollment.infrastructure.controller.dto;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de salida para representar inscripciones en las respuestas.
 * Incluye todos los datos de la inscripción.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de una inscripción")
public class EnrollmentOutputDto {

    @Schema(
            description = "ID del estudiante inscrito",
            example = "1"
    )
    private Long studentId;

    @Schema(
            description = "ID del grupo al que está inscrito",
            example = "1"
    )
    private Long groupId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora de inscripción",
            example = "2024-03-20T10:30:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime enrolledAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora de última actualización",
            example = "2024-03-20T10:30:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime updatedAt;

    @Schema(
            description = "Estado actual de la inscripción",
            example = "ACTIVE",
            allowableValues = {"PENDING_PAYMENT", "ACTIVE", "COMPLETED", "CANCELLED_BY_ADMIN", "CANCELLED_BY_STUDENT"}
    )
    private EnrollmentStatus status;
}