package com.acainfo.backend.groupcreationrequest.infrastructure.repository.mapper;

import com.acainfo.backend.groupcreationrequest.domain.entity.GroupCreationRequest;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.jpa.entity.GroupCreationRequestJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para convertir entre entidades JPA y entidades de dominio.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface GroupCreationRequestJpaMapper {

    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    GroupCreationRequest toDomain(GroupCreationRequestJpa jpa);

    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    GroupCreationRequestJpa toJpa(GroupCreationRequest domain);

    /**
     * Convierte una lista de entidades JPA a lista de entidades de dominio
     */
    List<GroupCreationRequest> toDomainList(List<GroupCreationRequestJpa> jpaList);

    /**
     * Convierte una lista de entidades de dominio a lista de entidades JPA
     */
    List<GroupCreationRequestJpa> toJpaList(List<GroupCreationRequest> domainList);

    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio
     * (útil para actualizaciones parciales)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestedAt", ignore = true)
    void updateJpaFromDomain(GroupCreationRequest domain, @MappingTarget GroupCreationRequestJpa jpa);
}