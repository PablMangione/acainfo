package com.acainfo.backend.subject.infrastructure.repository.imp;

import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.DuplicateSubjectException;
import com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException;
import com.acainfo.backend.subject.domain.exception.SubjectNotFoundException;
import com.acainfo.backend.subject.domain.repository.UpdateSubjectRepository;
import com.acainfo.backend.subject.infrastructure.repository.jpa.SubjectJpaRepository;
import com.acainfo.backend.subject.infrastructure.repository.jpa.entity.SubjectJpa;
import com.acainfo.backend.subject.infrastructure.repository.mapper.SubjectJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de actualización de asignaturas.
 * Adapta las operaciones de actualización del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UpdateSubjectRepositoryImp implements UpdateSubjectRepository {

    private final SubjectJpaRepository jpaRepository;
    private final SubjectJpaMapper mapper;

    /**
     * Actualiza una asignatura existente.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param subject la asignatura con los datos actualizados
     * @return la asignatura actualizada
     * @throws SubjectNotFoundException si la asignatura no existe
     * @throws DuplicateSubjectException si la actualización viola constraints únicos
     */
    @Override
    @Transactional
    public Subject update(Subject subject) {
        log.debug("Iniciando actualización de asignatura con ID: {}", subject.getId());

        // Validación defensiva mínima
        if (subject == null) {
            throw new InvalidSubjectDataException("La asignatura no puede ser null");
        }

        if (subject.getId() == 0L) {
            throw new InvalidSubjectDataException("El ID de la asignatura es requerido para actualizar");
        }

        try {
            // Buscar la entidad existente
            SubjectJpa existingEntity = jpaRepository.findById(subject.getId())
                    .orElseThrow(() -> {
                        log.error("No se encontró asignatura con ID: {}", subject.getId());
                        return new SubjectNotFoundException(
                                "No se encontró la asignatura con ID: " + subject.getId()
                        );
                    });

            // Actualizar los campos usando el mapper
            mapper.updateJpaFromDomain(subject, existingEntity);

            // Persistir los cambios
            SubjectJpa updatedEntity = jpaRepository.save(existingEntity);

            log.info("Asignatura actualizada exitosamente. ID: {}", updatedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(updatedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al actualizar asignatura: {}", e.getMessage());

            // Intentar determinar si es un error de constraint único
            if (e.getMessage() != null && e.getMessage().contains("uk_subject_name_major_year_quarter")) {
                throw new DuplicateSubjectException(
                        "Ya existe otra asignatura con la misma combinación de nombre, carrera, año y cuatrimestre"
                );
            }

            throw new InvalidSubjectDataException("Error de integridad en los datos");
        }
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de asignatura con ID: {}", id);
        return jpaRepository.existsById(id);
    }
}