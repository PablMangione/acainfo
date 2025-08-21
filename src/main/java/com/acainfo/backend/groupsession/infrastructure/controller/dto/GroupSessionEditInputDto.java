package com.acainfo.backend.groupsession.infrastructure.controller.dto;

import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO de entrada para la actualización de sesiones de grupo.
 * No permite cambiar el grupo al que pertenece la sesión.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para actualizar una sesión de grupo existente")
public class GroupSessionEditInputDto {

    @NotNull(message = "El día de la semana es obligatorio")
    @Schema(
            description = "Día de la semana en que se imparte la sesión",
            example = "MONDAY",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}
    )
    private DayOfWeek dayOfWeek;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Schema(
            description = "Hora de inicio de la sesión",
            example = "16:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalTime startTime;

    @NotNull(message = "La hora de fin es obligatoria")
    @Schema(
            description = "Hora de fin de la sesión",
            example = "18:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalTime endTime;

    @NotNull(message = "El aula es obligatoria")
    @Schema(
            description = "Aula donde se imparte la sesión",
            example = "PORTAL_1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"PORTAL_1", "PORTAL_2"}
    )
    private Classroom classroom;

    @NotNull(message = "El tipo de sesión es obligatorio")
    @Schema(
            description = "Tipo de sesión (presencial, online o dual)",
            example = "IN_PERSON",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"IN_PERSON", "DUAL", "ONLINE"}
    )
    private SessionType type;
}