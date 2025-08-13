package com.acainfo.backend.student.application;

/**
 * Caso de uso para la eliminación de estudiantes.
 * Define el contrato para las operaciones de eliminación de estudiantes.
 */
public interface DeleteStudentUseCase {

    /**
     * Elimina un estudiante por su ID.
     *
     * @param id el identificador del estudiante a eliminar
     * @throws com.acainfo.backend.student.domain.exception.StudentNotFoundException
     *         si no se encuentra el estudiante
     * @throws com.acainfo.backend.student.application.exception.StudentBusinessException
     *         si no se puede eliminar debido a dependencias (inscripciones activas, etc.)
     */
    void deleteById(Long id);

    /**
     * Elimina lógicamente un estudiante (soft delete).
     * Marca el estudiante como inactivo en lugar de eliminarlo físicamente.
     *
     * @param id el identificador del estudiante
     * @throws com.acainfo.backend.student.domain.exception.StudentNotFoundException
     *         si no se encuentra el estudiante
     * @throws com.acainfo.backend.student.application.exception.StudentBusinessException
     *         si el estudiante ya está inactivo
     */
    void softDeleteById(Long id);

    /**
     * Verifica si un estudiante puede ser eliminado.
     * Útil para validaciones previas en la UI.
     *
     * @param id el identificador del estudiante
     * @return true si puede ser eliminado, false si tiene dependencias
     */
    boolean canBeDeleted(Long id);
}