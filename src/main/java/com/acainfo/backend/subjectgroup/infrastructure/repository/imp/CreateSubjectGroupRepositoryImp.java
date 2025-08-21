package com.acainfo.backend.subjectgroup.infrastructure.repository.imp;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.domain.exception.DuplicateGroupException;
import com.acainfo.backend.subjectgroup.domain.exception.InvalidSubjectGroupDataException;
import com.acainfo.backend.subjectgroup.domain.repository.CreateSubjectGroupRepository;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.SubjectGroupJpaRepository;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import com.acainfo.backend.subjectgroup.infrastructure.repository.mapper.SubjectGroupJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de creación de grupos de asignatura.
 * Adapta la interfaz del dominio a la infraestructura de persistencia.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CreateSubjectGroupRepositoryImp implements CreateSubjectGroupRepository {

    private final SubjectGroupJpaRepository jpaRepository;
    private final SubjectGroupJpaMapper mapper;

    /**
     * Persiste un nuevo grupo de asignatura en el sistema.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param subjectGroup el grupo a crear
     * @return el grupo creado con su ID generado
     * @throws DuplicateGroupException si ya existe un grupo con el mismo nombre
     * @throws InvalidSubjectGroupDataException si ocurre un error de persistencia
     */
    @Override
    @Transactional
    public SubjectGroup save(SubjectGroup subjectGroup) {
        log.debug("Iniciando creación de grupo: {}", subjectGroup.getName());

        // Validación defensiva mínima
        if (subjectGroup == null) {
            throw new InvalidSubjectGroupDataException("El grupo no puede ser null");
        }

        try {
            // Convertir de dominio a JPA
            SubjectGroupJpa jpaEntity = mapper.toJpa(subjectGroup);

            // Persistir en base de datos
            SubjectGroupJpa savedEntity = jpaRepository.save(jpaEntity);

            log.info("Grupo creado exitosamente con ID: {}", savedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(savedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al crear grupo: {}", e.getMessage());

            // Intentar determinar si es un error de constraint único
            if (e.getMessage() != null && e.getMessage().contains("uk_group_name")) {
                throw new DuplicateGroupException(
                        String.format("Ya existe un grupo con el nombre '%s'",
                                subjectGroup.getName())
                );
            }

            // Verificar integridad referencial (profesor o asignatura no existen)
            if (e.getMessage() != null &&
                    (e.getMessage().contains("fk_group_subject") ||
                            e.getMessage().contains("subject_id"))) {
                throw new InvalidSubjectGroupDataException(
                        "La asignatura especificada no existe"
                );
            }

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
}