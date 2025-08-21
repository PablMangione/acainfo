package com.acainfo.backend.student.domain.repository;

/**
 * Interfaz que define las operaciones de eliminación para la entidad Student.
 *
 * Las restricciones de integridad referencial se manejan mediante
 * foreign keys y constraints en la base de datos.
 */
public interface DeleteStudentRepository {

    /**
     * Elimina un estudiante por su ID.
     *
     * @param id el identificador del estudiante
     * @return true si fue eliminado, false si no existía
     */
    boolean deleteById(Long id);

    /**
     * Elimina todos los estudiantes.
     * Usar con precaución, principalmente para tests.
     */
    void deleteAll();
}