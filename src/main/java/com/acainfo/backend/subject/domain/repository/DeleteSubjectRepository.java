package com.acainfo.backend.subject.domain.repository;

/**
 * Interfaz que define las operaciones de eliminación para la entidad Subject.
 *
 * Las restricciones de integridad referencial se manejan mediante
 * foreign keys y constraints en la base de datos.
 */
public interface DeleteSubjectRepository {

    /**
     * Elimina una asignatura por su ID.
     *
     * @param id el identificador de la asignatura
     * @return true si fue eliminada, false si no existía
     */
    boolean deleteById(Long id);

    /**
     * Elimina todas las asignaturas.
     * Usar con precaución, principalmente para tests.
     */
    void deleteAll();
}