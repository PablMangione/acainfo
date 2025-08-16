package com.acainfo.backend.enrollment.infrastructure.repository.mapper;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para convertir entre entidades JPA y entidades de dominio.
 * MapStruct generará la implementación en tiempo de compilación.
 *
 * Maneja la conversión del ID compuesto entre las capas.
 */
@Mapper(componentModel = "spring")
public interface EnrollmentJpaMapper {

    /**
     * Convierte una entidad JPA a una entidad de dominio.
     * Mapea los campos del ID compuesto.
     */
    @Mapping(source = "studentId", target = "id.studentId")
    @Mapping(source = "groupId", target = "id.groupId")
    Enrollment toDomain(EnrollmentJpa jpa);

    /**
     * Convierte una entidad de dominio a una entidad JPA.
     * Extrae los campos del ID compuesto.
     */
    @Mapping(source = "id.studentId", target = "studentId")
    @Mapping(source = "id.groupId", target = "groupId")
    EnrollmentJpa toJpa(Enrollment domain);

    /**
     * Convierte una lista de entidades JPA a lista de entidades de dominio
     */
    List<Enrollment> toDomainList(List<EnrollmentJpa> jpaList);

    /**
     * Convierte una lista de entidades de dominio a lista de entidades JPA
     */
    List<EnrollmentJpa> toJpaList(List<Enrollment> domainList);

    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio
     * (útil para actualizaciones parciales)
     */
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    void updateJpaFromDomain(Enrollment domain, @MappingTarget EnrollmentJpa jpa);

    /**
     * Convierte el ID compuesto de JPA a dominio
     */
    default EnrollmentId toEnrollmentId(Long studentId, Long groupId) {
        if (studentId == null || groupId == null) {
            return null;
        }
        return new EnrollmentId(studentId, groupId);
    }

    /**
     * Extrae el studentId del ID compuesto de dominio
     */
    default Long getStudentId(EnrollmentId id) {
        return id != null ? id.getStudentId() : null;
    }

    /**
     * Extrae el groupId del ID compuesto de dominio
     */
    default Long getGroupId(EnrollmentId id) {
        return id != null ? id.getGroupId() : null;
    }
}