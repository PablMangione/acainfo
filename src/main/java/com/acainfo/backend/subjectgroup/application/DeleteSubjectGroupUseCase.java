package com.acainfo.backend.subjectgroup.application;

/**
 * Caso de uso para la eliminación de grupos de asignatura.
 * Define el contrato para las operaciones de eliminación de grupos.
 */
public interface DeleteSubjectGroupUseCase {

    /**
     * Elimina un grupo por su ID.
     * Solo se permite si el grupo no tiene inscripciones activas.
     *
     * @param id el identificador del grupo a eliminar
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     * @throws com.acainfo.backend.subjectgroup.application.exception.SubjectGroupBusinessException
     *         si no se puede eliminar debido a inscripciones activas o sesiones programadas
     */
    void deleteById(Long id);

    /**
     * Cierra un grupo (soft delete).
     * Cambia el estado a CLOSED en lugar de eliminarlo físicamente.
     * Preferible a la eliminación física para mantener el histórico.
     *
     * @param id el identificador del grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.InvalidGroupStatusException
     *         si el grupo ya está cerrado
     */
    void closeGroup(Long id);

    /**
     * Verifica si un grupo puede ser eliminado.
     * Útil para validaciones previas en la UI.
     *
     * @param id el identificador del grupo
     * @return true si puede ser eliminado, false si tiene dependencias
     */
    boolean canBeDeleted(Long id);

    /**
     * Verifica si un grupo puede ser cerrado.
     *
     * @param id el identificador del grupo
     * @return true si puede ser cerrado, false si ya está cerrado
     */
    boolean canBeClosed(Long id);
}