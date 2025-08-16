package com.acainfo.backend.enrollment.application;

/**
 * Caso de uso para la eliminación de inscripciones.
 * Define el contrato para las operaciones de eliminación física de inscripciones.
 *
 * NOTA: En producción, generalmente se prefiere una eliminación lógica
 * (cambio de estado) en lugar de eliminación física. Este caso de uso
 * debe usarse con precaución y principalmente para:
 * - Limpieza de datos de prueba
 * - Cumplimiento de GDPR (derecho al olvido)
 * - Corrección de errores administrativos
 */
public interface DeleteEnrollmentUseCase {

    /**
     * Elimina físicamente una inscripción específica.
     * Esta operación es irreversible.
     *
     * @param studentId el ID del estudiante
     * @param groupId el ID del grupo
     * @return true si fue eliminada, false si no existía
     * @throws com.acainfo.backend.enrollment.application.exception.EnrollmentBusinessException
     *         si la inscripción está en estado ACTIVE o COMPLETED y no puede eliminarse
     */
    boolean delete(Long studentId, Long groupId);

    /**
     * Elimina todas las inscripciones de un estudiante.
     * Usar con extrema precaución, principalmente cuando se elimina el estudiante.
     *
     * @param studentId el ID del estudiante
     * @return número de inscripciones eliminadas
     * @throws com.acainfo.backend.enrollment.application.exception.EnrollmentBusinessException
     *         si alguna inscripción está activa y no puede eliminarse
     */
    int deleteAllByStudent(Long studentId);

    /**
     * Elimina todas las inscripciones de un grupo.
     * Usar con extrema precaución, principalmente cuando se elimina el grupo.
     *
     * @param groupId el ID del grupo
     * @return número de inscripciones eliminadas
     * @throws com.acainfo.backend.enrollment.application.exception.EnrollmentBusinessException
     *         si el grupo tiene inscripciones activas que no pueden eliminarse
     */
    int deleteAllByGroup(Long groupId);

    /**
     * Elimina todas las inscripciones canceladas más antiguas que el período especificado.
     * Útil para limpieza periódica de datos.
     *
     * @param daysOld número de días desde la cancelación
     * @return número de inscripciones eliminadas
     */
    int deleteCancelledOlderThan(int daysOld);

    /**
     * Elimina todas las inscripciones del sistema.
     * USAR SOLO EN ENTORNOS DE DESARROLLO/PRUEBAS.
     *
     * @throws com.acainfo.backend.enrollment.application.exception.EnrollmentBusinessException
     *         si se intenta ejecutar en producción
     */
    void deleteAll();
}