package com.acainfo.backend.groupsession.infrastructure.controller.dto;

import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO de salida para representar sesiones de grupo en las respuestas.
 * Incluye todos los datos de la sesión incluyendo timestamps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de una sesión de grupo")
public class GroupSessionOutputDto {

    @Schema(
            description = "Identificador único de la sesión",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "ID del grupo al que pertenece",
            example = "1"
    )
    private Long groupId;

    @Schema(
            description = "Día de la semana",
            example = "MONDAY"
    )
    private DayOfWeek dayOfWeek;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(
            description = "Hora de inicio",
            example = "16:00",
            type = "string",
            format = "time"
    )
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(
            description = "Hora de fin",
            example = "18:00",
            type = "string",
            format = "time"
    )
    private LocalTime endTime;

    @Schema(
            description = "Aula asignada",
            example = "PORTAL_1"
    )
    private Classroom classroom;

    @Schema(
            description = "Tipo de sesión",
            example = "IN_PERSON"
    )
    private SessionType type;

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
            description = "Día de la semana en español",
            example = "Lunes"
    )
    public String getDayOfWeekSpanish() {
        if (dayOfWeek == null) return "";
        return switch (dayOfWeek) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }

    @Schema(
            description = "Duración de la sesión en minutos",
            example = "120"
    )
    public Long getDurationInMinutes() {
        if (startTime == null || endTime == null) return 0L;
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    @Schema(
            description = "Horario formateado",
            example = "16:00 - 18:00"
    )
    public String getFormattedSchedule() {
        if (startTime == null || endTime == null) return "";
        return String.format("%s - %s", startTime, endTime);
    }

    @Schema(
            description = "Descripción del aula",
            example = "Portal 1"
    )
    public String getClassroomDescription() {
        if (classroom == null) return "";
        return switch (classroom) {
            case PORTAL_1 -> "Portal 1";
            case PORTAL_2 -> "Portal 2";
        };
    }

    @Schema(
            description = "Descripción del tipo de sesión",
            example = "Presencial"
    )
    public String getTypeDescription() {
        if (type == null) return "";
        return switch (type) {
            case IN_PERSON -> "Presencial";
            case ONLINE -> "Online";
            case DUAL -> "Dual";
        };
    }
}