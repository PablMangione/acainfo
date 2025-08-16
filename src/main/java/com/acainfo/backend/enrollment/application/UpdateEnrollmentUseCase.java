package com.acainfo.backend.enrollment.application;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import com.acainfo.backend.enrollment.infrastructure.controller.dto.EnrollmentOutputDto;
import com.acainfo.backend.enrollment.infrastructure.controller.dto.EnrollmentStatusUpdateDto;

/**
 * Caso de uso para la actualización de inscripciones.
 * Define el contrato para las operaciones de modificación de inscripciones existentes.
 */
public interface UpdateEnrollmentUseCase {

    /**
     * Actualiza el estado de una inscripción.
     *
     * @param studentId el ID del estudiante
     * @param groupId el ID del grupo
     * @param statusDto el nuevo estado de la inscripción
     * @return la inscripción actualizada
     * @throws com.acainfo.backend.enrollment.domain.exception.EnrollmentNotFoundException
     *         si no existe la inscripción
     * @throws com.acainfo.backend.enrollment.application.exception.InvalidStatusTransitionException
     *         si la transición de estado no es válida
     */
    EnrollmentOutputDto updateStatus(Long studentId, Long groupId, EnrollmentStatusUpdateDto statusDto);

    /**
     * Confirma el pago de una inscripción pendiente.
     * Cambia el estado de PENDING_PAYMENT a ACTIVE.
     *
     * @param studentId el ID del estudiante
     * @param groupId el ID del grupo
     * @param paymentReference referencia del pago procesado
     * @return la inscripción actualizada con estado ACTIVE
     * @throws com.acainfo.backend.enrollment.domain.exception.EnrollmentNotFoundException
     *         si no existe la inscripción
     * @throws com.acainfo.backend.enrollment.application.exception.InvalidStatusTransitionException
     *         si la inscripción no está en estado PENDING_PAYMENT
     */
    EnrollmentOutputDto confirmPayment(Long studentId, Long groupId, String paymentReference);

    /**
     * Marca una inscripción como completada.
     * Se usa cuando el estudiante finaliza el curso.
     *
     * @param studentId el ID del estudiante
     * @param groupId el ID del grupo
     * @return la inscripción actualizada con estado COMPLETED
     * @throws com.acainfo.backend.enrollment.domain.exception.EnrollmentNotFoundException
     *         si no existe la inscripción
     * @throws com.acainfo.backend.enrollment.application.exception.InvalidStatusTransitionException
     *         si la inscripción no está en estado ACTIVE
     */
    EnrollmentOutputDto markAsCompleted(Long studentId, Long groupId);

    /**
     * Cancela una inscripción por parte del administrador.
     *
     * @param studentId el ID del estudiante
     * @param groupId el ID del grupo
     * @param reason motivo de la cancelación
     * @return la inscripción actualizada con estado CANCELLED_BY_ADMIN
     * @throws com.acainfo.backend.enrollment.domain.exception.EnrollmentNotFoundException
     *         si no existe la inscripción
     * @throws com.acainfo.backend.enrollment.application.exception.InvalidStatusTransitionException
     *         si la inscripción ya está cancelada o completada
     */
    EnrollmentOutputDto cancelByAdmin(Long studentId, Long groupId, String reason);

    /**
     * Cancela una inscripción por parte del estudiante.
     *
     * @param studentId el ID del estudiante
     * @param groupId el ID del grupo
     * @return la inscripción actualizada con estado CANCELLED_BY_STUDENT
     * @throws com.acainfo.backend.enrollment.domain.exception.EnrollmentNotFoundException
     *         si no existe la inscripción
     * @throws com.acainfo.backend.enrollment.application.exception.InvalidStatusTransitionException
     *         si la inscripción ya está cancelada o completada
     * @throws com.acainfo.backend.enrollment.application.exception.EnrollmentBusinessException
     *         si ha pasado el período permitido para cancelación
     */
    EnrollmentOutputDto cancelByStudent(Long studentId, Long groupId);

    /**
     * Actualiza el estado de todas las inscripciones pendientes de pago
     * que han excedido el tiempo límite.
     *
     * @return número de inscripciones actualizadas
     */
    int cancelExpiredPendingPayments();
}