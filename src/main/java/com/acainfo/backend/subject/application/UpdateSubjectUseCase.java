package com.acainfo.backend.subject.application;

import com.acainfo.backend.subject.domain.entity.Subject;

/**
 * Caso de uso para la actualización de asignaturas.
 *
 * Define el contrato para las operaciones de modificación de asignaturas existentes.
 * Trabaja exclusivamente con entidades de dominio.
 */
public interface UpdateSubjectUseCase {

    /**
     * Actualiza una asignatura existente.
     *
     * Solo se actualizan los campos modificables:
     * - name
     * - major
     * - courseYear
     * - quarter
     * - isActive
     *
     * No se actualizan:
     * - id (inmutable)
     * - createdAt (inmutable)
     * - updatedAt (se actualiza automáticamente)
     *
     * @param subject la asignatura con los datos actualizados (debe incluir ID válido)
     * @return la asignatura actualizada con los cambios persistidos
     *
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura con el ID proporcionado
     *
     * @throws com.acainfo.backend.subject.domain.exception.DuplicateSubjectException
     *         si la actualización genera una duplicación con otra asignatura existente
     *
     * @throws com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException
     *         si los datos proporcionados no son válidos según las reglas de negocio
     *
     * @throws IllegalArgumentException
     *         si subject es null o no tiene ID
     */
    Subject update(Subject subject);

    /**
     * Actualización parcial de una asignatura.
     * Solo actualiza los campos no nulos del objeto proporcionado.
     *
     * @param id el identificador de la asignatura a actualizar
     * @param partialSubject objeto con solo los campos a actualizar (los demás pueden ser null)
     * @return la asignatura actualizada
     *
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura
     *
     * @throws com.acainfo.backend.subject.domain.exception.DuplicateSubjectException
     *         si la actualización genera una duplicación
     *
     * @throws IllegalArgumentException
     *         si el id es null o <= 0
     */
    Subject partialUpdate(Long id, Subject partialSubject);

    /**
     * Activa o desactiva una asignatura.
     *
     * Reglas de negocio:
     * - No se puede desactivar si tiene grupos activos
     * - No se puede desactivar si tiene inscripciones pendientes
     *
     * @param id el identificador de la asignatura
     * @param active true para activar, false para desactivar
     * @return la asignatura con el estado actualizado
     *
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura
     *
     * @throws com.acainfo.backend.subject.application.exception.SubjectBusinessException
     *         si no se puede cambiar el estado debido a reglas de negocio
     *
     * @throws IllegalArgumentException
     *         si el id es null o <= 0
     */
    Subject updateActiveStatus(Long id, boolean active);

    /**
     * Valida si una asignatura puede ser actualizada.
     * Útil para validaciones previas en la UI.
     *
     * @param subject la asignatura con los cambios propuestos
     * @return true si la actualización es válida, false en caso contrario
     */
    boolean canBeUpdated(Subject subject);

    /**
     * Valida si una asignatura puede ser desactivada.
     * Verifica que no tenga dependencias activas.
     *
     * @param id el identificador de la asignatura
     * @return true si puede ser desactivada, false si tiene dependencias
     */
    boolean canBeDeactivated(Long id);
}