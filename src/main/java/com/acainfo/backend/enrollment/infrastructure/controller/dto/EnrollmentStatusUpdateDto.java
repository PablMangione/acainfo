package com.acainfo.backend.enrollment.infrastructure.controller.dto;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO de entrada para la actualización del estado de inscripciones.
 * Solo permite cambiar el estado de la inscripción.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar el estado de una inscripción")
public class EnrollmentStatusUpdateDto {

    @NotNull(message = "El estado es obligatorio")
    @Schema(
            description = "Nuevo estado de la inscripción",
            example = "ACTIVE",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"PENDING_PAYMENT", "ACTIVE", "COMPLETED", "CANCELLED_BY_ADMIN", "CANCELLED_BY_STUDENT"}
    )
    private EnrollmentStatus status;
}