package com.acainfo.backend.enrollment.domain.repository;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;
import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de lectura para la entidad Enrollment.
 *
 * Contiene operaciones de consulta sin efectos secundarios.
 * Los filtrados complejos y validaciones se manejan en la capa de servicio.
 */
public interface ReadEnrollmentRepository {

    /**
     * Busca una inscripción por su ID compuesto.
     */
    Optional<Enrollment> findById(EnrollmentId id);

    /**
     * Obtiene todas las inscripciones.
     */
    List<Enrollment> findAll();

    /**
     * Verifica si existe una inscripción con el ID compuesto dado.
     */
    boolean existsById(EnrollmentId id);

    /**
     * Busca todas las inscripciones de un estudiante.
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     * Busca todas las inscripciones de un grupo.
     */
    List<Enrollment> findByGroupId(Long groupId);

    /**
     * Busca inscripciones por estado.
     */
    List<Enrollment> findByStatus(EnrollmentStatus status);

    /**
     * Busca inscripciones de un estudiante por estado.
     */
    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    /**
     * Busca inscripciones de un grupo por estado.
     */
    List<Enrollment> findByGroupIdAndStatus(Long groupId, EnrollmentStatus status);

    /**
     * Cuenta el número de inscripciones activas en un grupo.
     * Útil para verificar la capacidad disponible.
     */
    long countByGroupIdAndStatus(Long groupId, EnrollmentStatus status);

    /**
     * Verifica si un estudiante está inscrito en un grupo específico.
     */
    boolean existsByStudentIdAndGroupId(Long studentId, Long groupId);

    /**
     * Busca inscripciones activas de un estudiante.
     * Útil para generar el horario del estudiante.
     */
    List<Enrollment> findActiveEnrollmentsByStudentId(Long studentId);

    /**
     * Busca inscripciones pendientes de pago.
     * Útil para procesos de gestión y recordatorios.
     */
    List<Enrollment> findPendingPaymentEnrollments();
}