package com.acainfo.backend.subjectgroup.infrastructure.repository.imp;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.domain.exception.DuplicateGroupException;
import com.acainfo.backend.subjectgroup.domain.exception.InvalidSubjectGroupDataException;
import com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException;
import com.acainfo.backend.subjectgroup.domain.repository.UpdateSubjectGroupRepository;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.SubjectGroupJpaRepository;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import com.acainfo.backend.subjectgroup.infrastructure.repository.mapper.SubjectGroupJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de actualización de grupos de asignatura.
 * Adapta las operaciones de actualización del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UpdateSubjectGroupRepositoryImp implements UpdateSubjectGroupRepository {

    private final SubjectGroupJpaRepository jpaRepository;
    private final SubjectGroupJpaMapper mapper;

    /**
     * Actualiza un grupo existente.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param subjectGroup el grupo con los datos actualizados
     * @return el grupo actualizado
     * @throws SubjectGroupNotFoundException si el grupo no existe
     * @throws DuplicateGroupException si la actualización viola constraints únicos
     */
    @Override
    @Transactional
    public SubjectGroup update(SubjectGroup subjectGroup) {
        log.debug("Iniciando actualización de grupo con ID: {}", subjectGroup.getId());

        // Validación defensiva mínima
        if (subjectGroup == null) {
            throw new InvalidSubjectGroupDataException("El grupo no puede ser null");
        }

        if (subjectGroup.getId() == null || subjectGroup.getId() == 0L) {
            throw new InvalidSubjectGroupDataException("El ID del grupo es requerido para actualizar");
        }

        try {
            // Buscar la entidad existente
            SubjectGroupJpa existingEntity = jpaRepository.findById(subjectGroup.getId())
                    .orElseThrow(() -> {
                        log.error("No se encontró grupo con ID: {}", subjectGroup.getId());
                        return new SubjectGroupNotFoundException(
                                "No se encontró el grupo con ID: " + subjectGroup.getId()
                        );
                    });

            // Actualizar los campos usando el mapper
            mapper.updateJpaFromDomain(subjectGroup, existingEntity);

            // Persistir los cambios
            SubjectGroupJpa updatedEntity = jpaRepository.save(existingEntity);

            log.info("Grupo actualizado exitosamente. ID: {}", updatedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(updatedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al actualizar grupo: {}", e.getMessage());

            // Verificar si es un error de constraint único
            if (e.getMessage() != null && e.getMessage().contains("uk_group_name")) {
                throw new DuplicateGroupException(
                        "Ya existe otro grupo con el nombre proporcionado"
                );
            }

            // Verificar integridad referencial
            if (e.getMessage() != null &&
                    (e.getMessage().contains("fk_group_teacher") ||
                            e.getMessage().contains("teacher_id"))) {
                throw new InvalidSubjectGroupDataException(
                        "El profesor especificado no existe"
                );
            }

            throw new InvalidSubjectGroupDataException("Error de integridad en los datos");
        }
    }

    @Override
    @Transactional
    public boolean incrementEnrollmentCount(Long groupId) {
        log.debug("Incrementando contador de inscripciones para grupo ID: {}", groupId);

        int rowsAffected = jpaRepository.incrementEnrollmentCount(groupId);
        boolean success = rowsAffected > 0;

        if (success) {
            log.debug("Contador incrementado exitosamente para grupo ID: {}", groupId);
        } else {
            log.warn("No se pudo incrementar el contador para grupo ID: {} (posiblemente lleno)", groupId);
        }

        return success;
    }

    @Override
    @Transactional
    public boolean decrementEnrollmentCount(Long groupId) {
        log.debug("Decrementando contador de inscripciones para grupo ID: {}", groupId);

        int rowsAffected = jpaRepository.decrementEnrollmentCount(groupId);
        boolean success = rowsAffected > 0;

        if (success) {
            log.debug("Contador decrementado exitosamente para grupo ID: {}", groupId);
        } else {
            log.warn("No se pudo decrementar el contador para grupo ID: {} (posiblemente en 0)", groupId);
        }

        return success;
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de grupo con ID: {}", id);
        return jpaRepository.existsById(id);
    }
}