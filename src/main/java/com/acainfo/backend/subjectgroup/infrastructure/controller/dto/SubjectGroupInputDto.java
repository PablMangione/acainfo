package com.acainfo.backend.subjectgroup.infrastructure.controller.dto;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO de entrada para la creación de grupos de asignatura.
 * Contiene las validaciones de formato para los datos de entrada.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear un nuevo grupo de asignatura")
public class SubjectGroupInputDto {

    @NotBlank(message = "El nombre del grupo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(
            description = "Nombre identificativo del grupo",
            example = "Álgebra Lineal - Grupo A",
            minLength = 3,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @NotNull(message = "La asignatura es obligatoria")
    @Positive(message = "El ID de la asignatura debe ser positivo")
    @Schema(
            description = "ID de la asignatura asociada",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long subjectId;

    @NotNull(message = "El profesor es obligatorio")
    @Positive(message = "El ID del profesor debe ser positivo")
    @Schema(
            description = "ID del profesor que impartirá el grupo",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long teacherId;

    @NotNull(message = "El tipo de grupo es obligatorio")
    @Schema(
            description = "Tipo de grupo (regular o intensivo)",
            example = "REGULAR",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"REGULAR", "INTENSIVE"}
    )
    private GroupType type;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad mínima es 1 estudiante")
    @Max(value = 30, message = "La capacidad máxima es 30 estudiantes")
    @Schema(
            description = "Número máximo de estudiantes en el grupo",
            example = "15",
            minimum = "1",
            maximum = "30",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer maxCapacity;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @DecimalMax(value = "999.99", message = "El precio no puede exceder 999.99")
    @Digits(integer = 3, fraction = 2, message = "El precio debe tener máximo 3 dígitos enteros y 2 decimales")
    @Schema(
            description = "Precio mensual del grupo en euros",
            example = "45.00",
            minimum = "0.01",
            maximum = "999.99",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal price;

    @Builder.Default
    @Schema(
            description = "Estado inicial del grupo",
            example = "PLANNED",
            defaultValue = "PLANNED",
            allowableValues = {"PLANNED", "ACTIVE", "CLOSED"},
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private GroupStatus status = GroupStatus.PLANNED;
}