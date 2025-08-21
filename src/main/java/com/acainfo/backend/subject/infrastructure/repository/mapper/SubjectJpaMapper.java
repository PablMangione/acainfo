package com.acainfo.backend.subject.infrastructure.repository.mapper;

import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.infrastructure.repository.jpa.entity.SubjectJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para convertir entre entidades JPA y entidades de dominio.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface SubjectJpaMapper {
    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    Subject toDomain(SubjectJpa jpa);

    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    @Mapping(target = "groups", ignore = true)
    SubjectJpa toJpa(Subject domain);

    /**
     * Convierte una lista de entidades JPA a lista de entidades de dominio
     */
    List<Subject> toDomainList(List<SubjectJpa> jpaList);

    /**
     * Convierte una lista de entidades de dominio a lista de entidades JPA
     */
    List<SubjectJpa> toJpaList(List<Subject> domainList);

    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio
     * (útil para actualizaciones parciales)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "groups", ignore = true)
    void updateJpaFromDomain(Subject domain, @MappingTarget SubjectJpa jpa);

}