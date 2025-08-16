package com.acainfo.backend.enrollment.domain.repository;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;

/**
 * Interfaz que define las operaciones de actualización para la entidad Enrollment.
 *
 * Las validaciones de integridad se manejan mediante constraints en la base de datos.
 * Las validaciones de formato se realizan en los DTOs de entrada.
 */
public interface UpdateEnrollmentRepository {

    /**
     * Actualiza una inscripción existente.
     *
     * @param enrollment la inscripción con los datos actualizados
     * @return la inscripción actualizada
     */
    Enrollment update(Enrollment enrollment);

    /**
     * Verifica si una inscripción existe antes de intentar actualizarla.
     * Útil para proporcionar mensajes de error más claros.
     */
    boolean existsById(EnrollmentId id);
}