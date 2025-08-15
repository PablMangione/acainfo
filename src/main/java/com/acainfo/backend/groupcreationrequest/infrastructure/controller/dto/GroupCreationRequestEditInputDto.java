package com.acainfo.backend.groupcreationrequest.infrastructure.controller.dto;

import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO de entrada para la actualización del estado de solicitudes de creación de grupo.
 * Solo permite cambiar el estado de la solicitud.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar el estado de una solicitud de creación de grupo")
public class GroupCreationRequestEditInputDto {

    @NotNull(message = "El estado es obligatorio")
    @Schema(
            description = "Nuevo estado de la solicitud",
            example = "APPROVED",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"PENDING", "APPROVED", "REJECTED", "CANCELLED"}
    )
    private RequestStatus status;
}