package com.acainfo.backend.subject.infrastructure.repository.imp;

import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.DuplicateSubjectException;
import com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException;
import com.acainfo.backend.subject.domain.repository.CreateSubjectRepository;
import com.acainfo.backend.subject.infrastructure.repository.jpa.SubjectJpaRepository;
import com.acainfo.backend.subject.infrastructure.repository.jpa.entity.SubjectJpa;
import com.acainfo.backend.subject.infrastructure.repository.mapper.SubjectJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de creación de asignaturas.
 * Adapta la interfaz del dominio a la infraestructura de persistencia.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CreateSubjectRepositoryImp implements CreateSubjectRepository {

    private final SubjectJpaRepository jpaRepository;
    private final SubjectJpaMapper mapper;

    /**
     * Persiste una nueva asignatura en el sistema.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param subject la asignatura a crear
     * @return la asignatura creada con su ID generado
     * @throws DuplicateSubjectException si ya existe una asignatura con la misma combinación única
     * @throws InvalidSubjectDataException si ocurre un error de persistencia
     */
    @Override
    @Transactional
    public Subject save(Subject subject) {
        log.debug("Iniciando creación de asignatura: {}", subject);

        // Validación defensiva mínima
        if (subject == null) {
            throw new InvalidSubjectDataException("La asignatura no puede ser null");
        }

        try {
            // Convertir de dominio a JPA
            SubjectJpa jpaEntity = mapper.toJpa(subject);

            // Persistir en base de datos
            SubjectJpa savedEntity = jpaRepository.save(jpaEntity);

            log.info("Asignatura creada exitosamente con ID: {}", savedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(savedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al crear asignatura: {}", e.getMessage());

            // Intentar determinar si es un error de constraint único
            if (e.getMessage() != null && e.getMessage().contains("uk_subject_name_major_year_quarter")) {
                throw new DuplicateSubjectException(
                        String.format("Ya existe una asignatura '%s' para la combinación especificada",
                                subject.getName())
                );
            }

            throw new InvalidSubjectDataException("Error de integridad en los datos");
        }
    }
}