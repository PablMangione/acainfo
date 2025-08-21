package com.acainfo.backend.teacher.infrastructure.repository.mapper;

import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.entity.TeacherJpa;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TeacherJpaMapper {

    /**
     * JPA → Domain
     */
    @Mapping(target = "admin", source = "isAdmin")
    Teacher toDomain(TeacherJpa jpa);

    /**
     * Domain → JPA
     */
    @Mapping(target = "isAdmin", source = "admin")
    @Mapping(target = "groups", ignore = true) // Manejado por JPA
    TeacherJpa toJpa(Teacher domain);

    /**
     * Actualizar JPA desde Domain
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "isAdmin", source = "admin")
    void updateJpaFromDomain(Teacher domain, @MappingTarget TeacherJpa jpa);

    /**
     * Conversión de listas
     */
    List<Teacher> toDomainList(List<TeacherJpa> jpaList);
    List<TeacherJpa> toJpaList(List<Teacher> domainList);
}