package com.acainfo.backend.enrollment.infrastructure.repository.mapper;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EnrollmentJpaMapper {

    /**
     * Convierte una entidad JPA a una entidad de dominio.
     * Usa método default para manejar la clave compuesta manualmente.
     */
    default Enrollment toDomain(EnrollmentJpa jpa) {
        if (jpa == null) {
            return null;
        }

        // Crear el ID del dominio desde el ID embebido
        EnrollmentId domainId = new EnrollmentId(
                jpa.getId().getStudentId(),
                jpa.getId().getGroupId()
        );

        // Crear la entidad de dominio
        return Enrollment.builder()
                .id(domainId)
                .enrolledAt(jpa.getEnrolledAt())
                .updatedAt(jpa.getUpdatedAt())
                .status(jpa.getStatus())
                .build();
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA básica.
     * NOTA: Este método NO setea las relaciones student y group.
     * Debe ser completado en el repositorio con las entidades reales.
     */
    default EnrollmentJpa toJpa(Enrollment domain) {
        if (domain == null) {
            return null;
        }

        // Crear el ID embebido
        EnrollmentJpa.EnrollmentId embeddedId = new EnrollmentJpa.EnrollmentId();
        embeddedId.setStudentId(domain.getId().getStudentId());
        embeddedId.setGroupId(domain.getId().getGroupId());

        // Crear la entidad JPA
        EnrollmentJpa jpa = new EnrollmentJpa();
        jpa.setId(embeddedId);
        jpa.setEnrolledAt(domain.getEnrolledAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        jpa.setStatus(domain.getStatus());

        // NOTA: student y group deben ser seteados en el repositorio
        return jpa;
    }

    /**
     * Convierte una lista de entidades JPA a lista de entidades de dominio
     */
    default List<Enrollment> toDomainList(List<EnrollmentJpa> jpaList) {
        if (jpaList == null) {
            return null;
        }
        return jpaList.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de entidades de dominio a lista de entidades JPA
     */
    default List<EnrollmentJpa> toJpaList(List<Enrollment> domainList) {
        if (domainList == null) {
            return null;
        }
        return domainList.stream()
                .map(this::toJpa)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una entidad JPA existente con los datos de una entidad de dominio.
     * Solo actualiza los campos modificables.
     */
    default void updateJpaFromDomain(Enrollment domain, @MappingTarget EnrollmentJpa jpa) {
        if (domain == null || jpa == null) {
            return;
        }

        // Solo actualizar el status, los timestamps se manejan automáticamente
        jpa.setStatus(domain.getStatus());
        // updatedAt se actualiza automáticamente con @UpdateTimestamp
    }

    /**
     * Método helper para crear una entidad JPA completa con referencias.
     * Este método debe ser usado por los repositorios al crear nuevas inscripciones.
     */
    default EnrollmentJpa toJpaWithReferences(Enrollment domain,
                                              StudentJpa student,
                                              SubjectGroupJpa group) {
        if (domain == null) {
            return null;
        }

        EnrollmentJpa jpa = toJpa(domain);
        jpa.setStudent(student);
        jpa.setGroup(group);

        return jpa;
    }
}