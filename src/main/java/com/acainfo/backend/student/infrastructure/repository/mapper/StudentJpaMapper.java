package com.acainfo.backend.student.infrastructure.repository.mapper;

import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // Ignora campos no mapeados
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StudentJpaMapper {

    /**
     * Convierte de JPA a dominio
     */
    @Mapping(target = "active", source = "isActive")
    Student toDomain(StudentJpa jpa);

    /**
     * Convierte de dominio a JPA
     */
    @Mapping(target = "isActive", source = "active")
    @Mapping(target = "enrollments", ignore = true) // Se maneja por JPA
    StudentJpa toJpa(Student domain);

    /**
     * Actualiza una entidad JPA desde el dominio
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "isActive", source = "active")
    void updateJpaFromDomain(Student domain, @MappingTarget StudentJpa jpa);

    /**
     * Conversión de listas
     */
    List<Student> toDomainList(List<StudentJpa> jpaList);
    List<StudentJpa> toJpaList(List<Student> domainList);
}