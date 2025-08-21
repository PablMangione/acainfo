package com.acainfo.backend.enrollment.infrastructure.repository.imp;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;
import com.acainfo.backend.enrollment.domain.repository.UpdateEnrollmentRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.EnrollmentJpaRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import com.acainfo.backend.enrollment.infrastructure.repository.mapper.EnrollmentJpaMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UpdateEnrollmentRepositoryImp implements UpdateEnrollmentRepository {

    private final EnrollmentJpaRepository jpaRepository;
    private final EnrollmentJpaMapper mapper;

    @Override
    public Enrollment update(Enrollment enrollment) {
        log.info("Actualizando inscripción con ID: [{}, {}]",
                enrollment.getId().getStudentId(),
                enrollment.getId().getGroupId());

        try {
            // Crear el ID embebido
            EnrollmentJpa.EnrollmentId embeddedId = new EnrollmentJpa.EnrollmentId();
            embeddedId.setStudentId(enrollment.getId().getStudentId());
            embeddedId.setGroupId(enrollment.getId().getGroupId());

            // Buscar la entidad existente usando el ID compuesto
            EnrollmentJpa existingEntity = jpaRepository
                    .findById(embeddedId)
                    .orElseThrow(() -> {
                        log.error("No se encontró la inscripción con ID: [{}, {}]",
                                enrollment.getId().getStudentId(),
                                enrollment.getId().getGroupId());
                        return new EntityNotFoundException(
                                String.format("Inscripción no encontrada para estudiante %d en grupo %d",
                                        enrollment.getId().getStudentId(),
                                        enrollment.getId().getGroupId())
                        );
                    });

            // Actualizar los campos modificables (no se actualizan IDs ni enrolledAt)
            mapper.updateJpaFromDomain(enrollment, existingEntity);

            // Guardar los cambios
            EnrollmentJpa updatedEntity = jpaRepository.save(existingEntity);

            log.info("Inscripción actualizada exitosamente. Estado: {}",
                    updatedEntity.getStatus());

            // Convertir de vuelta a dominio
            return mapper.toDomain(updatedEntity);

        } catch (EntityNotFoundException e) {
            log.error("Inscripción no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al actualizar inscripción: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar la inscripción", e);
        }
    }

    @Override
    public boolean existsById(EnrollmentId id) {
        log.debug("Verificando existencia de inscripción con ID: [{}, {}]",
                id.getStudentId(), id.getGroupId());

        // Crear el ID embebido
        EnrollmentJpa.EnrollmentId embeddedId = new EnrollmentJpa.EnrollmentId();
        embeddedId.setStudentId(id.getStudentId());
        embeddedId.setGroupId(id.getGroupId());

        return jpaRepository.existsById(embeddedId);
    }
}