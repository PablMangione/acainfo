package com.acainfo.backend.student.infrastructure.repository.mapper;

import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para convertir entre entidades JPA y entidades de dominio.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface StudentJpaMapper {

    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    Student toDomain(StudentJpa jpa);

    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    StudentJpa toJpa(Student domain);

    /**
     * Convierte una lista de entidades JPA a lista de entidades de dominio
     */
    List<Student> toDomainList(List<StudentJpa> jpaList);

    /**
     * Convierte una lista de entidades de dominio a lista de entidades JPA
     */
    List<StudentJpa> toJpaList(List<Student> domainList);

    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio
     * (útil para actualizaciones parciales)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    void updateJpaFromDomain(Student domain, @MappingTarget StudentJpa jpa);
}