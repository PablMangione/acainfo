package com.acainfo.backend.groupsession.infrastructure.repository.mapper;

import com.acainfo.backend.groupsession.domain.entity.GroupSession;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.entity.GroupSessionJpa;
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
public interface GroupSessionJpaMapper {

    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    @Mapping(source = "group.id", target = "groupId")
    GroupSession toDomain(GroupSessionJpa jpa);

    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    @Mapping(source = "groupId", target = "group")
    GroupSessionJpa toJpa(GroupSession domain);

    /**
     * Convierte una lista de entidades JPA a lista de entidades de dominio
     */
    List<GroupSession> toDomainList(List<GroupSessionJpa> jpaList);

    /**
     * Convierte una lista de entidades de dominio a lista de entidades JPA
     */
    List<GroupSessionJpa> toJpaList(List<GroupSession> domainList);

    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio
     * (útil para actualizaciones parciales)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "group", ignore = true) // No actualizar la relación en updates
    void updateJpaFromDomain(GroupSession domain, @MappingTarget GroupSessionJpa jpa);

    /**
     * Método auxiliar para mapear el groupId a la entidad SubjectGroupJpa
     */
    default SubjectGroupJpa mapGroupId(Long groupId) {
        if (groupId == null) return null;
        SubjectGroupJpa group = new SubjectGroupJpa();
        group.setId(groupId);
        return group;
    }
}