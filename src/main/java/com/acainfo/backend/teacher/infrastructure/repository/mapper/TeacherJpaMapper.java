package com.acainfo.backend.teacher.infrastructure.repository.mapper;

import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.entity.TeacherJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para convertir entre entidades JPA y entidades de dominio.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface TeacherJpaMapper {

    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    Teacher toDomain(TeacherJpa jpa);

    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    TeacherJpa toJpa(Teacher domain);

    /**
     * Convierte una lista de entidades JPA a lista de entidades de dominio
     */
    List<Teacher> toDomainList(List<TeacherJpa> jpaList);

    /**
     * Convierte una lista de entidades de dominio a lista de entidades JPA
     */
    List<TeacherJpa> toJpaList(List<Teacher> domainList);

    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio
     * (útil para actualizaciones parciales)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    void updateJpaFromDomain(Teacher domain, @MappingTarget TeacherJpa jpa);
}