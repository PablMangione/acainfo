package com.acainfo.backend.teacher.application;

/**
 * Caso de uso para la eliminación de profesores.
 * Define el contrato para las operaciones de eliminación de profesores.
 */
public interface DeleteTeacherUseCase {

    /**
     * Elimina un profesor por su ID.
     *
     * @param id el identificador del profesor a eliminar
     * @throws com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException
     *         si no se encuentra el profesor
     * @throws com.acainfo.backend.teacher.application.exception.TeacherBusinessException
     *         si no se puede eliminar debido a dependencias (grupos asignados)
     *         o si es el último administrador
     */
    void deleteById(Long id);

    /**
     * Verifica si un profesor puede ser eliminado.
     * Útil para validaciones previas en la UI.
     *
     * @param id el identificador del profesor
     * @return true si puede ser eliminado, false si tiene dependencias o es el último admin
     */
    boolean canBeDeleted(Long id);
}