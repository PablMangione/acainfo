package com.acainfo.backend.groupcreationrequest.domain.repository;

import com.acainfo.backend.groupcreationrequest.domain.entity.GroupCreationRequest;

/**
 * Interfaz que define las operaciones de creación para la entidad GroupCreationRequest.
 *
 * Parte del patrón CRUD segregado siguiendo ISP (Interface Segregation Principle).
 * Las validaciones de unicidad se manejan a nivel de base de datos mediante
 * constraints, y las validaciones de formato en los DTOs de entrada.
 */
public interface CreateGroupCreationRequestRepository {

    /**
     * Persiste una nueva solicitud de creación de grupo en el sistema.
     *
     * @param groupCreationRequest la solicitud a crear
     * @return la solicitud creada con su ID generado
     */
    GroupCreationRequest save(GroupCreationRequest groupCreationRequest);
}