package com.acainfo.backend.enrollment.application;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import com.acainfo.backend.enrollment.infrastructure.controller.dto.EnrollmentOutputDto;

import java.util.List;

/**
 * Caso de uso para la consulta de inscripciones.
 * Define el contrato para las operaciones de lectura de inscripciones.
 */
public interface ReadEnrollmentUseCase {

    /**
     * Obtiene una inscripción específica por estudiante y grupo.
     *
     * @param studentId el ID del estudiante
     * @param groupId el ID del grupo
     * @return la inscripción encontrada
     * @throws com.acainfo.backend.enrollment.domain.exception.EnrollmentNotFoundException
     *         si no existe la inscripción
     */
    EnrollmentOutputDto findByStudentAndGroup(Long studentId, Long groupId);

    /**
     * Obtiene todas las inscripciones de un estudiante.
     *
     * @param studentId el ID del estudiante
     * @return lista de inscripciones del estudiante, vacía si no tiene
     */
    List<EnrollmentOutputDto> findByStudent(Long studentId);

    /**
     * Obtiene todas las inscripciones de un grupo.
     *
     * @param groupId el ID del grupo
     * @return lista de inscripciones del grupo, vacía si no tiene
     */
    List<EnrollmentOutputDto> findByGroup(Long groupId);

    /**
     * Obtiene inscripciones por estado.
     *
     * @param status el estado de las inscripciones a buscar
     * @return lista de inscripciones con el estado especificado
     */
    List<EnrollmentOutputDto> findByStatus(EnrollmentStatus status);

    /**
     * Obtiene las inscripciones activas de un estudiante.
     * Útil para generar el horario del estudiante.
     *
     * @param studentId el ID del estudiante
     * @return lista de inscripciones activas del estudiante
     */
    List<EnrollmentOutputDto> findActiveByStudent(Long studentId);

    /**
     * Obtiene las inscripciones de un estudiante con un estado específico.
     *
     * @param studentId el ID del estudiante
     * @param status el estado de las inscripciones
     * @return lista de inscripciones del estudiante con el estado especificado
     */
    List<EnrollmentOutputDto> findByStudentAndStatus(Long studentId, EnrollmentStatus status);

    /**
     * Obtiene las inscripciones de un grupo con un estado específico.
     *
     * @param groupId el ID del grupo
     * @param status el estado de las inscripciones
     * @return lista de inscripciones del grupo con el estado especificado
     */
    List<EnrollmentOutputDto> findByGroupAndStatus(Long groupId, EnrollmentStatus status);

    /**
     * Verifica si un estudiante está inscrito en un grupo.
     *
     * @param studentId el ID del estudiante
     * @param groupId el ID del grupo
     * @return true si existe la inscripción, false en caso contrario
     */
    boolean existsEnrollment(Long studentId, Long groupId);

    /**
     * Cuenta las inscripciones activas de un grupo.
     * Útil para verificar disponibilidad.
     *
     * @param groupId el ID del grupo
     * @return número de inscripciones activas en el grupo
     */
    long countActiveEnrollments(Long groupId);

    /**
     * Obtiene todas las inscripciones pendientes de pago.
     * Útil para procesos administrativos y recordatorios.
     *
     * @return lista de inscripciones con estado PENDING_PAYMENT
     */
    List<EnrollmentOutputDto> findPendingPayments();
}