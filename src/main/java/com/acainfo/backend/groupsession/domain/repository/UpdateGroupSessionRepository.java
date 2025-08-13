package com.acainfo.backend.groupsession.domain.repository;

import com.acainfo.backend.groupsession.domain.entity.GroupSession;

/**
 * Interfaz que define las operaciones de actualización para la entidad GroupSession.
 *
 * Las validaciones de integridad se manejan mediante constraints en la base de datos.
 * Las validaciones de formato se realizan en los DTOs de entrada.
 */
public interface UpdateGroupSessionRepository {

    /**
     * Actualiza una sesión existente.
     *
     * @param groupSession la sesión con los datos actualizados
     * @return la sesión actualizada
     */
    GroupSession update(GroupSession groupSession);

    /**
     * Verifica si una sesión existe antes de intentar actualizarla.
     */
    boolean existsById(Long id);
}