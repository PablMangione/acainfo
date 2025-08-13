package com.acainfo.backend.student.infrastructure.controller.dto;

import com.acainfo.backend.globalenum.Major;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de salida para representar estudiantes en las respuestas.
 * Incluye todos los datos del estudiante excepto la contraseña.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un estudiante")
public class StudentOutputDto {

    @Schema(
            description = "Identificador único del estudiante",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nombre del estudiante",
            example = "María"
    )
    private String name;

    @Schema(
            description = "Apellido del estudiante",
            example = "García López"
    )
    private String lastName;

    @Schema(
            description = "Correo electrónico del estudiante",
            example = "maria.garcia@estudiante.edu"
    )
    private String email;

    @Schema(
            description = "Número de teléfono del estudiante",
            example = "+34612345678"
    )
    private String phoneNumber;

    @Schema(
            description = "Carrera que cursa el estudiante",
            example = "ING_INF"
    )
    private Major major;

    @Schema(
            description = "Indica si el estudiante está activo",
            example = "true"
    )
    private Boolean isActive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora de registro en el sistema",
            example = "2024-01-15T10:30:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime registeredAt;

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
            description = "Nombre completo del estudiante",
            example = "María García López"
    )
    public String getFullName() {
        return name + " " + lastName;
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

    @Schema(
            description = "Iniciales del estudiante para avatares",
            example = "MG"
    )
    public String getInitials() {
        if ((name == null || name.trim().isEmpty()) &&
                (lastName == null || lastName.trim().isEmpty())) {
            return "";
        }

        String firstInitial = (name != null && !name.trim().isEmpty())
                ? name.trim().substring(0, 1).toUpperCase()
                : "";

        String lastInitial = (lastName != null && !lastName.trim().isEmpty())
                ? lastName.trim().substring(0, 1).toUpperCase()
                : "";

        return firstInitial + lastInitial;
    }

    @Schema(
            description = "Estado del estudiante en texto",
            example = "Activo"
    )
    public String getStatusText() {
        return isActive != null && isActive ? "Activo" : "Inactivo";
    }
}