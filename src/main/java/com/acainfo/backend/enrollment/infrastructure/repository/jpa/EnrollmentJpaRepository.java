package com.acainfo.backend.enrollment.infrastructure.repository.jpa;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Enrollment.
 * Define las operaciones de persistencia y consultas personalizadas.
 *
 * Utiliza la clase EnrollmentIdJpa como tipo de ID por la clave compuesta.
 */
@Repository
public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentJpa, EnrollmentJpa.EnrollmentIdJpa> {

    /**
     * Busca una inscripción por student_id y group_id.
     */
    @Query("SELECT e FROM EnrollmentJpa e WHERE e.studentId = :studentId AND e.groupId = :groupId")
    Optional<EnrollmentJpa> findByStudentIdAndGroupId(
            @Param("studentId") Long studentId,
            @Param("groupId") Long groupId
    );

    /**
     * Busca todas las inscripciones de un estudiante.
     */
    List<EnrollmentJpa> findByStudentIdOrderByEnrolledAtDesc(Long studentId);

    /**
     * Busca todas las inscripciones de un grupo.
     */
    List<EnrollmentJpa> findByGroupIdOrderByEnrolledAtDesc(Long groupId);

    /**
     * Busca inscripciones por estado.
     */
    List<EnrollmentJpa> findByStatusOrderByEnrolledAtDesc(EnrollmentStatus status);

    /**
     * Busca inscripciones de un estudiante por estado.
     */
    List<EnrollmentJpa> findByStudentIdAndStatusOrderByEnrolledAtDesc(
            Long studentId,
            EnrollmentStatus status
    );

    /**
     * Busca inscripciones de un grupo por estado.
     */
    List<EnrollmentJpa> findByGroupIdAndStatusOrderByEnrolledAtDesc(
            Long groupId,
            EnrollmentStatus status
    );

    /**
     * Cuenta el número de inscripciones en un grupo con un estado específico.
     */
    long countByGroupIdAndStatus(Long groupId, EnrollmentStatus status);

    /**
     * Verifica si existe una inscripción para un estudiante en un grupo.
     */
    boolean existsByStudentIdAndGroupId(Long studentId, Long groupId);

    /**
     * Busca inscripciones activas de un estudiante.
     */
    @Query("SELECT e FROM EnrollmentJpa e WHERE e.studentId = :studentId " +
            "AND e.status = 'ACTIVE' ORDER BY e.enrolledAt DESC")
    List<EnrollmentJpa> findActiveEnrollmentsByStudentId(@Param("studentId") Long studentId);

    /**
     * Busca inscripciones pendientes de pago.
     */
    @Query("SELECT e FROM EnrollmentJpa e WHERE e.status = 'PENDING_PAYMENT' " +
            "ORDER BY e.enrolledAt ASC")
    List<EnrollmentJpa> findPendingPaymentEnrollments();

    /**
     * Elimina una inscripción específica.
     */
    void deleteByStudentIdAndGroupId(Long studentId, Long groupId);

    /**
     * Elimina todas las inscripciones de un estudiante.
     */
    int deleteByStudentId(Long studentId);

    /**
     * Elimina todas las inscripciones de un grupo.
     */
    int deleteByGroupId(Long groupId);

    /**
     * Busca inscripciones de un estudiante en grupos de una asignatura específica.
     * Útil para evitar inscripciones duplicadas en la misma asignatura.
     */
    @Query("SELECT e FROM EnrollmentJpa e " +
            "JOIN SubjectGroupJpa g ON e.groupId = g.id " +
            "WHERE e.studentId = :studentId " +
            "AND g.subjectId = :subjectId " +
            "AND e.status IN ('ACTIVE', 'PENDING_PAYMENT')")
    List<EnrollmentJpa> findByStudentIdAndSubjectId(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId
    );

    /**
     * Cuenta las inscripciones activas de un grupo.
     * Útil para verificar disponibilidad.
     */
    @Query("SELECT COUNT(e) FROM EnrollmentJpa e " +
            "WHERE e.groupId = :groupId " +
            "AND e.status = 'ACTIVE'")
    long countActiveEnrollmentsByGroupId(@Param("groupId") Long groupId);
}