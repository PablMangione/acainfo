package com.acainfo.backend.enrollment.domain.entity;

import lombok.*;

import java.io.Serializable;

/**
 * Value Object que representa la clave compuesta de una inscripción.
 * Identifica únicamente una inscripción por la combinación de estudiante y grupo.
 */
@Getter
@Setter  // Necesario para que MapStruct pueda setear los valores
@Builder
@NoArgsConstructor  // Constructor vacío necesario
@AllArgsConstructor  // Constructor con todos los argumentos
@EqualsAndHashCode
@ToString
public class EnrollmentId implements Serializable {

    private Long studentId;
    private Long groupId;

    /**
     * Factory method para crear un nuevo ID de inscripción.
     */
    public static EnrollmentId of(Long studentId, Long groupId) {
        return new EnrollmentId(studentId, groupId);
    }

    /**
     * Valida que el ID sea válido.
     */
    public boolean isValid() {
        return studentId != null && studentId > 0
                && groupId != null && groupId > 0;
    }
}