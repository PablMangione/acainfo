package com.acainfo.backend.subject.infrastructure.controller.dto;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO de entrada para la actualización de asignaturas.
 * Similar a SubjectInputDto pero sin incluir el estado activo (se maneja aparte).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para actualizar una asignatura existente")
public class SubjectEditInputDto {

    @NotBlank(message = "El nombre de la asignatura es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(
            description = "Nombre de la asignatura",
            example = "Álgebra Lineal",
            minLength = 3,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @NotNull(message = "La carrera es obligatoria")
    @Schema(
            description = "Carrera a la que pertenece la asignatura",
            example = "ING_INF",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"ING_INF", "ING_IND"}
    )
    private Major major;

    @NotNull(message = "El año del curso es obligatorio")
    @Schema(
            description = "Año del curso en el que se imparte",
            example = "FIRST",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"FIRST", "SECOND", "THIRD", "FOURTH"}
    )
    private CourseYear courseYear;

    @NotNull(message = "El cuatrimestre es obligatorio")
    @Schema(
            description = "Cuatrimestre en el que se imparte",
            example = "FIRST",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"FIRST", "SECOND"}
    )
    private Quarter quarter;
}