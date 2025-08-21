package com.acainfo.backend.groupsession.domain.repository;

import com.acainfo.backend.groupsession.domain.entity.GroupSession;

/**
 * Interfaz que define las operaciones de creación para la entidad GroupSession.
 *
 * Parte del patrón CRUD segregado siguiendo ISP (Interface Segregation Principle).
 * Las validaciones de unicidad se manejan a nivel de base de datos mediante
 * constraints, y las validaciones de formato en los DTOs de entrada.
 */
public interface CreateGroupSessionRepository {

    /**
     * Persiste una nueva sesión de grupo en el sistema.
     *
     * @param groupSession la sesión a crear
     * @return la sesión creada con su ID generado
     */
    GroupSession save(GroupSession groupSession);
}