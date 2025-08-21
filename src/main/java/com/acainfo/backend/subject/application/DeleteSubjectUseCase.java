package com.acainfo.backend.subject.application;

/**
 * Caso de uso para la eliminación de asignaturas.
 *
 * Define el contrato para las operaciones de eliminación de asignaturas.
 *
 * IMPORTANTE: Se recomienda usar soft delete (desactivación) en lugar de
 * eliminación física para mantener la integridad histórica del sistema.
 */
public interface DeleteSubjectUseCase {

    /**
     * Elimina físicamente una asignatura del sistema.
     *
     * ADVERTENCIA: Esta operación es irreversible y puede afectar la integridad
     * histórica. Se recomienda usar desactivación en su lugar.
     *
     * Reglas de negocio:
     * - No se puede eliminar si tiene grupos asociados (activos o inactivos)
     * - No se puede eliminar si tiene inscripciones históricas
     * - No se puede eliminar si tiene solicitudes de creación de grupo
     *
     * @param id el identificador de la asignatura a eliminar
     * @return true si se eliminó exitosamente, false si no existía
     *
     * @throws com.acainfo.backend.subject.application.exception.SubjectBusinessException
     *         si no se puede eliminar debido a restricciones de integridad
     *
     * @throws com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException
     *         si hay restricciones de integridad referencial en la BD
     *
     * @throws IllegalArgumentException
     *         si el id es null o <= 0
     */
    boolean deleteById(Long id);

    /**
     * Realiza un soft delete (desactivación) de la asignatura.
     *
     * Esta es la opción preferida ya que mantiene el registro histórico.
     * Equivale a llamar updateActiveStatus(id, false) del UpdateSubjectUseCase.
     *
     * @param id el identificador de la asignatura
     * @return la asignatura desactivada
     *
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura
     *
     * @throws com.acainfo.backend.subject.application.exception.SubjectBusinessException
     *         si no se puede desactivar debido a dependencias activas
     *
     * @throws IllegalArgumentException
     *         si el id es null o <= 0
     */
    com.acainfo.backend.subject.domain.entity.Subject softDelete(Long id);

    /**
     * Verifica si una asignatura puede ser eliminada físicamente.
     *
     * @param id el identificador de la asignatura
     * @return true si puede ser eliminada, false si tiene dependencias
     * @throws IllegalArgumentException si el id es null o <= 0
     */
    boolean canBeDeleted(Long id);

    /**
     * Verifica si una asignatura puede ser desactivada (soft delete).
     *
     * @param id el identificador de la asignatura
     * @return true si puede ser desactivada, false si tiene dependencias activas
     * @throws IllegalArgumentException si el id es null o <= 0
     */
    boolean canBeSoftDeleted(Long id);

    /**
     * Elimina todas las asignaturas inactivas que no tienen dependencias.
     *
     * Útil para limpieza de base de datos.
     * Solo elimina asignaturas que:
     * - Están inactivas (isActive = false)
     * - No tienen grupos asociados
     * - No tienen inscripciones históricas
     * - Fueron desactivadas hace más de X días (configurable)
     *
     * @param daysInactive número mínimo de días inactiva para ser elegible
     * @return número de asignaturas eliminadas
     * @throws IllegalArgumentException si daysInactive <= 0
     */
    int purgeInactiveSubjects(int daysInactive);
}