package com.acainfo.backend.teacher.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de salida para representar profesores en las respuestas.
 * Incluye todos los datos del profesor excepto la contraseña.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un profesor")
public class TeacherOutputDto {

    @Schema(
            description = "Identificador único del profesor",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nombre completo del profesor",
            example = "Juan Pérez García"
    )
    private String name;

    @Schema(
            description = "Indica si el profesor tiene permisos de administrador",
            example = "false"
    )
    private Boolean isAdmin;

    @Schema(
            description = "Correo electrónico del profesor",
            example = "juan.perez@universidad.edu"
    )
    private String email;

    @Schema(
            description = "Número de teléfono del profesor",
            example = "+34612345678"
    )
    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
            description = "Fecha y hora de registro en el sistema",
            example = "2024-01-15T10:30:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime registeredAt;

    // Campos calculados o derivados útiles para el frontend

    @Schema(
            description = "Rol del profesor en el sistema",
            example = "Profesor"
    )
    public String getRole() {
        return isAdmin != null && isAdmin ? "Administrador" : "Profesor";
    }

    @Schema(
            description = "Iniciales del profesor para avatares",
            example = "JP"
    )
    public String getInitials() {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }

        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }

        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    @Schema(
            description = "Nombre formateado para mostrar",
            example = "Prof. Juan Pérez García"
    )
    public String getDisplayName() {
        String prefix = (isAdmin != null && isAdmin) ? "Admin." : "Prof.";
        return prefix + " " + name;
    }
}