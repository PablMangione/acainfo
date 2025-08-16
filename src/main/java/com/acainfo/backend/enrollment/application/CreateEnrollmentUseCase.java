package com.acainfo.backend.enrollment.application;

import com.acainfo.backend.enrollment.infrastructure.controller.dto.EnrollmentInputDto;
import com.acainfo.backend.enrollment.infrastructure.controller.dto.EnrollmentOutputDto;

/**
 * Caso de uso para la creación de inscripciones.
 * Define el contrato para inscribir estudiantes en grupos.
 */
public interface CreateEnrollmentUseCase {

    /**
     * Crea una nueva inscripción de un estudiante en un grupo.
     *
     * @param inputDto datos de la inscripción a crear
     * @return la inscripción creada con estado PENDING_PAYMENT
     * @throws com.acainfo.backend.enrollment.domain.exception.DuplicateEnrollmentException
     *         si el estudiante ya está inscrito en el grupo
     * @throws com.acainfo.backend.enrollment.domain.exception.EnrollmentNotFoundException
     *         si no existe el estudiante o el grupo
     * @throws com.acainfo.backend.enrollment.application.exception.EnrollmentBusinessException
     *         si el grupo no tiene plazas disponibles o no está activo
     */
    EnrollmentOutputDto create(EnrollmentInputDto inputDto);

    /**
     * Crea una inscripción y procesa el pago inmediatamente.
     * Útil para inscripciones con pago directo.
     *
     * @param inputDto datos de la inscripción a crear
     * @param paymentToken token de pago para procesar
     * @return la inscripción creada con estado ACTIVE si el pago es exitoso
     * @throws com.acainfo.backend.enrollment.domain.exception.DuplicateEnrollmentException
     *         si el estudiante ya está inscrito en el grupo
     * @throws com.acainfo.backend.enrollment.application.exception.PaymentException
     *         si el pago no se puede procesar
     */
    EnrollmentOutputDto createWithPayment(EnrollmentInputDto inputDto, String paymentToken);
}