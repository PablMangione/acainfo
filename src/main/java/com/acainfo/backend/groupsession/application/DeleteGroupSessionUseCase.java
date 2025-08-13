package com.acainfo.backend.groupsession.application;

/**
 * Caso de uso para la eliminación de sesiones de grupo.
 * Define el contrato para las operaciones de eliminación de sesiones.
 */
public interface DeleteGroupSessionUseCase {

    /**
     * Elimina una sesión por su ID.
     *
     * @param id el identificador de la sesión a eliminar
     * @throws com.acainfo.backend.groupsession.domain.exception.GroupSessionNotFoundException
     *         si no se encuentra la sesión
     */
    void deleteById(Long id);

    /**
     * Elimina todas las sesiones de un grupo.
     * Útil cuando se cierra o elimina un grupo.
     *
     * @param groupId el identificador del grupo
     */
    void deleteByGroupId(Long groupId);

    /**
     * Verifica si una sesión puede ser eliminada.
     *
     * @param id el identificador de la sesión
     * @return true si puede ser eliminada, false en caso contrario
     */
    boolean canBeDeleted(Long id);
}