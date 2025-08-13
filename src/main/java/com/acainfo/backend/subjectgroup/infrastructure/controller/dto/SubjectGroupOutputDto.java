package com.acainfo.backend.subjectgroup.infrastructure.controller.dto;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida para representar grupos de asignatura en las respuestas.
 * Incluye todos los datos del grupo incluyendo timestamps y estadísticas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un grupo de asignatura")
public class SubjectGroupOutputDto {

    @Schema(
            description = "Identificador único del grupo",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nombre identificativo del grupo",
            example = "Álgebra Lineal - Grupo A"
    )
    private String name;

    @Schema(
            description = "ID de la asignatura asociada",
            example = "1"
    )
    private Long subjectId;

    @Schema(
            description = "ID del profesor que imparte el grupo",
            example = "1"
    )
    private Long teacherId;

    @Schema(
            description = "Estado actual del grupo",
            example = "ACTIVE"
    )
    private GroupStatus status;

    @Schema(
            description = "Tipo de grupo",
            example = "REGULAR"
    )
    private GroupType type;

    @Schema(
            description = "Capacidad máxima de estudiantes",
            example = "15"
    )
    private Integer maxCapacity;

    @Schema(
            description = "Número actual de estudiantes inscritos",
            example = "12"
    )
    private Integer currentEnrollments;

    @Schema(
            description = "Precio mensual en euros",
            example = "45.00"
    )
    private BigDecimal price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora de creación",
            example = "2024-01-15T10:30:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora de última actualización",
            example = "2024-01-20T14:45:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime updatedAt;

    // Campos calculados o derivados útiles para el frontend

    @Schema(
            description = "Número de plazas disponibles",
            example = "3"
    )
    public Integer getAvailableSpots() {
        if (maxCapacity == null || currentEnrollments == null) {
            return 0;
        }
        return maxCapacity - currentEnrollments;
    }

    @Schema(
            description = "Indica si el grupo tiene plazas disponibles",
            example = "true"
    )
    public boolean hasAvailableSpots() {
        return getAvailableSpots() > 0;
    }

    @Schema(
            description = "Porcentaje de ocupación del grupo",
            example = "80.0"
    )
    public Double getOccupancyPercentage() {
        if (maxCapacity == null || maxCapacity == 0 || currentEnrollments == null) {
            return 0.0;
        }
        return (currentEnrollments.doubleValue() / maxCapacity.doubleValue()) * 100;
    }

    @Schema(
            description = "Indica si el grupo está completo",
            example = "false"
    )
    public boolean isFull() {
        if (maxCapacity == null || currentEnrollments == null) {
            return false;
        }
        return currentEnrollments >= maxCapacity;
    }

    @Schema(
            description = "Descripción del estado en texto legible",
            example = "Activo"
    )
    public String getStatusDescription() {
        if (status == null) {
            return "Desconocido";
        }
        return switch (status) {
            case PLANNED -> "Planificado";
            case ACTIVE -> "Activo";
            case CLOSED -> "Cerrado";
        };
    }

    @Schema(
            description = "Descripción del tipo en texto legible",
            example = "Regular"
    )
    public String getTypeDescription() {
        if (type == null) {
            return "Desconocido";
        }
        return type == GroupType.REGULAR ? "Regular" : "Intensivo";
    }

    @Schema(
            description = "Indica si se pueden aceptar nuevas inscripciones",
            example = "true"
    )
    public boolean canAcceptEnrollments() {
        return status == GroupStatus.ACTIVE && hasAvailableSpots();
    }
}