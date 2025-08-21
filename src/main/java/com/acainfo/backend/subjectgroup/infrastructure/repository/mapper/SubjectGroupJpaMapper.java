package com.acainfo.backend.subjectgroup.infrastructure.repository.mapper;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SubjectGroupJpaMapper {

    /**
     * JPA → Domain
     */
    @Mapping(target = "subjectId", source = "subject.id")
    @Mapping(target = "teacherId", source = "teacher.id")
    SubjectGroup toDomain(SubjectGroupJpa jpa);

    /**
     * Domain → JPA (básico, sin relaciones)
     */
    @Mapping(target = "subject", ignore = true) // Se setea en el repositorio
    @Mapping(target = "teacher", ignore = true) // Se setea en el repositorio
    @Mapping(target = "enrollments", ignore = true) // Manejado por JPA
    @Mapping(target = "sessions", ignore = true) // Manejado por JPA
    SubjectGroupJpa toJpa(SubjectGroup domain);

    /**
     * Actualizar JPA desde Domain
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    void updateJpaFromDomain(SubjectGroup domain, @MappingTarget SubjectGroupJpa jpa);

    /**
     * Conversión de listas
     */
    List<SubjectGroup> toDomainList(List<SubjectGroupJpa> jpaList);
    List<SubjectGroupJpa> toJpaList(List<SubjectGroup> domainList);
}