package com.acainfo.backend.enrollment.infrastructure.repository.imp;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.repository.CreateEnrollmentRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.EnrollmentJpaRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import com.acainfo.backend.enrollment.infrastructure.repository.mapper.EnrollmentJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de creación de inscripciones.
 * Adapta las operaciones de persistencia del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateEnrollmentRepositoryImp implements CreateEnrollmentRepository {

    private final EnrollmentJpaRepository jpaRepository;
    private final EnrollmentJpaMapper mapper;

    @Override
    public Enrollment save(Enrollment enrollment) {
        log.info("Creando nueva inscripción para estudiante {} en grupo {}",
                enrollment.getId().getStudentId(),
                enrollment.getId().getGroupId());

        try {
            // Verificar si ya existe una inscripción
            boolean exists = jpaRepository.existsByStudentIdAndGroupId(
                    enrollment.getId().getStudentId(),
                    enrollment.getId().getGroupId()
            );

            if (exists) {
                log.error("Ya existe una inscripción para el estudiante {} en el grupo {}",
                        enrollment.getId().getStudentId(),
                        enrollment.getId().getGroupId());
                throw new DataIntegrityViolationException(
                        "Ya existe una inscripción para este estudiante en este grupo"
                );
            }

            // Convertir a entidad JPA
            EnrollmentJpa jpaEntity = mapper.toJpa(enrollment);

            // Persistir
            EnrollmentJpa savedEntity = jpaRepository.save(jpaEntity);
            log.info("Inscripción creada exitosamente con ID: [{}, {}]",
                    savedEntity.getStudentId(),
                    savedEntity.getGroupId());

            // Convertir de vuelta a dominio
            return mapper.toDomain(savedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al crear inscripción: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear inscripción: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear la inscripción", e);
        }
    }
}