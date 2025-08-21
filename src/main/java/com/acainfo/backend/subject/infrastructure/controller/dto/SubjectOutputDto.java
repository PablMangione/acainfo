package com.acainfo.backend.subject.infrastructure.controller.dto;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de salida para representar asignaturas en las respuestas.
 * Incluye todos los datos de la asignatura incluyendo timestamps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de una asignatura")
public class SubjectOutputDto {

    @Schema(
            description = "Identificador único de la asignatura",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nombre de la asignatura",
            example = "Álgebra Lineal"
    )
    private String name;

    @Schema(
            description = "Carrera a la que pertenece",
            example = "ING_INF"
    )
    private Major major;

    @Schema(
            description = "Año del curso",
            example = "FIRST"
    )
    private CourseYear courseYear;

    @Schema(
            description = "Cuatrimestre",
            example = "FIRST"
    )
    private Quarter quarter;

    @Schema(
            description = "Estado de la asignatura",
            example = "true"
    )
    private Boolean isActive;

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

    // Campos calculados o derivados que pueden ser útiles en el frontend

    @Schema(
            description = "Descripción legible del curso",
            example = "1º año - 1º cuatrimestre"
    )
    public String getCourseDescription() {
        String year = switch (courseYear) {
            case FIRST -> "1º";
            case SECOND -> "2º";
            case THIRD -> "3º";
            case FOURTH -> "4º";
        };

        String quarterText = quarter == Quarter.FIRST ? "1º" : "2º";

        return year + " año - " + quarterText + " cuatrimestre";
    }

    @Schema(
            description = "Nombre completo de la carrera",
            example = "Ingeniería Informática"
    )
    public String getMajorFullName() {
        return switch (major) {
            case ING_INF -> "Ingeniería Informática";
            case ING_IND -> "Ingeniería Industrial";
        };
    }
}