package com.acainfo.backend.subject.application;

/**
 * Caso de uso para la eliminación de asignaturas.
 * Define el contrato para las operaciones de eliminación de asignaturas.
 */
public interface DeleteSubjectUseCase {

    /**
     * Elimina una asignatura por su ID.
     *
     * @param id el identificador de la asignatura a eliminar
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura
     * @throws com.acainfo.backend.subject.application.exception.SubjectBusinessException
     *         si no se puede eliminar debido a dependencias (grupos, inscripciones, etc.)
     */
    void deleteById(long id);

    /**
     * Elimina lógicamente una asignatura (soft delete).
     * Marca la asignatura como inactiva en lugar de eliminarla físicamente.
     *
     * @param id el identificador de la asignatura
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura
     * @throws com.acainfo.backend.subject.application.exception.SubjectBusinessException
     *         si la asignatura ya está inactiva
     */
    void softDeleteById(long id);

    /**
     * Verifica si una asignatura puede ser eliminada.
     * Útil para validaciones previas en la UI.
     *
     * @param id el identificador de la asignatura
     * @return true si puede ser eliminada, false si tiene dependencias
     */
    boolean canBeDeleted(long id);
}