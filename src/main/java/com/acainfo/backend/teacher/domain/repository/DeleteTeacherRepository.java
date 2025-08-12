package com.acainfo.backend.teacher.domain.repository;

/**
 * Interfaz que define las operaciones de eliminación para la entidad Teacher.
 *
 * Las restricciones de integridad referencial se manejan mediante
 * foreign keys y constraints en la base de datos.
 */
public interface DeleteTeacherRepository {

    /**
     * Elimina un profesor por su ID.
     *
     * @param id el identificador del profesor
     * @return true si fue eliminado, false si no existía
     */
    boolean deleteById(Long id);

    /**
     * Elimina todos los profesores.
     * Usar con precaución, principalmente para tests.
     */
    void deleteAll();
}