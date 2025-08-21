package com.acainfo.backend.groupsession.infrastructure.controller.dto;

import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO de entrada para la creación de sesiones de grupo.
 * Contiene las validaciones de formato para los datos de entrada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear una nueva sesión de grupo")
public class GroupSessionInputDto {

    @NotNull(message = "El grupo es obligatorio")
    @Schema(
            description = "ID del grupo al que pertenece la sesión",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long groupId;

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

    @Builder.Default
    @Schema(
            description = "Tipo de sesión (presencial, online o dual)",
            example = "IN_PERSON",
            defaultValue = "IN_PERSON",
            allowableValues = {"IN_PERSON", "DUAL", "ONLINE"},
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private SessionType type = SessionType.IN_PERSON;
}