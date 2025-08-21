package com.acainfo.backend.groupsession.domain.repository;

/**
 * Interfaz que define las operaciones de eliminación para la entidad GroupSession.
 *
 * Las restricciones de integridad referencial se manejan mediante
 * foreign keys y constraints en la base de datos.
 */
public interface DeleteGroupSessionRepository {

    /**
     * Elimina una sesión por su ID.
     *
     * @param id el identificador de la sesión
     * @return true si fue eliminada, false si no existía
     */
    boolean deleteById(Long id);

    /**
     * Elimina todas las sesiones de un grupo.
     *
     * @param groupId el identificador del grupo
     */
    void deleteByGroupId(Long groupId);

    /**
     * Elimina todas las sesiones.
     * Usar con precaución, principalmente para tests.
     */
    void deleteAll();
}