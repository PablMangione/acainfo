package com.acainfo.backend.subjectgroup.infrastructure.repository.mapper;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para convertir entre entidades JPA y entidades de dominio.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface SubjectGroupJpaMapper {

    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    SubjectGroup toDomain(SubjectGroupJpa jpa);

    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    SubjectGroupJpa toJpa(SubjectGroup domain);

    /**
     * Convierte una lista de entidades JPA a lista de entidades de dominio
     */
    List<SubjectGroup> toDomainList(List<SubjectGroupJpa> jpaList);

    /**
     * Convierte una lista de entidades de dominio a lista de entidades JPA
     */
    List<SubjectGroupJpa> toJpaList(List<SubjectGroup> domainList);

    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio
     * (útil para actualizaciones parciales)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateJpaFromDomain(SubjectGroup domain, @MappingTarget SubjectGroupJpa jpa);
}