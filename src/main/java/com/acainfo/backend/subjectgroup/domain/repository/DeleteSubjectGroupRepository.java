package com.acainfo.backend.subjectgroup.domain.repository;

/**
 * Interfaz que define las operaciones de eliminación para la entidad SubjectGroup.
 *
 * Las restricciones de integridad referencial se manejan mediante
 * foreign keys y constraints en la base de datos.
 */
public interface DeleteSubjectGroupRepository {

    /**
     * Elimina un grupo por su ID.
     * Solo se permite si no tiene inscripciones activas.
     *
     * @param id el identificador del grupo
     * @return true si fue eliminado, false si no existía
     */
    boolean deleteById(Long id);

    /**
     * Elimina todos los grupos.
     * Usar con precaución, principalmente para tests.
     */
    void deleteAll();

    /**
     * Verifica si un grupo puede ser eliminado.
     * Un grupo no puede eliminarse si tiene inscripciones activas
     * o sesiones programadas.
     *
     * @param id el identificador del grupo
     * @return true si puede ser eliminado
     */
    boolean canBeDeleted(Long id);
}