package com.acainfo.backend.enrollment.infrastructure.repository.imp;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.repository.CreateEnrollmentRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.EnrollmentJpaRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import com.acainfo.backend.enrollment.infrastructure.repository.mapper.EnrollmentJpaMapper;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            // Crear el ID embebido
            EnrollmentJpa.EnrollmentId embeddedId = new EnrollmentJpa.EnrollmentId();
            embeddedId.setStudentId(enrollment.getId().getStudentId());
            embeddedId.setGroupId(enrollment.getId().getGroupId());

            // Verificar si ya existe usando el ID compuesto
            if (jpaRepository.existsById(embeddedId)) {
                log.error("Ya existe una inscripción para el estudiante {} en el grupo {}",
                        enrollment.getId().getStudentId(),
                        enrollment.getId().getGroupId());
                throw new DataIntegrityViolationException(
                        "Ya existe una inscripción para este estudiante en este grupo"
                );
            }

            // Crear la entidad JPA
            EnrollmentJpa jpaEntity = new EnrollmentJpa();
            jpaEntity.setId(embeddedId);

            // Establecer las relaciones (con @MapsId esto se sincroniza automáticamente)
            StudentJpa student = new StudentJpa();
            student.setId(enrollment.getId().getStudentId());
            jpaEntity.setStudent(student);

            SubjectGroupJpa group = new SubjectGroupJpa();
            group.setId(enrollment.getId().getGroupId());
            jpaEntity.setGroup(group);

            // Establecer otros campos
            jpaEntity.setStatus(enrollment.getStatus());

            // Persistir
            EnrollmentJpa savedEntity = jpaRepository.save(jpaEntity);
            log.info("Inscripción creada exitosamente con ID: [{}, {}]",
                    savedEntity.getId().getStudentId(),
                    savedEntity.getId().getGroupId());

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