package com.acainfo.backend.teacher.infrastructure.repository.imp;

import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.domain.exception.DuplicateTeacherException;
import com.acainfo.backend.teacher.domain.exception.InvalidTeacherDataException;
import com.acainfo.backend.teacher.domain.repository.CreateTeacherRepository;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.TeacherJpaRepository;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.entity.TeacherJpa;
import com.acainfo.backend.teacher.infrastructure.repository.mapper.TeacherJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de creación de profesores.
 * Adapta la interfaz del dominio a la infraestructura de persistencia.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CreateTeacherRepositoryImp implements CreateTeacherRepository {

    private final TeacherJpaRepository jpaRepository;
    private final TeacherJpaMapper mapper;

    /**
     * Persiste un nuevo profesor en el sistema.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param teacher el profesor a crear
     * @return el profesor creado con su ID generado
     * @throws DuplicateTeacherException si ya existe un profesor con el mismo email
     * @throws InvalidTeacherDataException si ocurre un error de persistencia
     */
    @Override
    @Transactional
    public Teacher save(Teacher teacher) {
        log.debug("Iniciando creación de profesor: {}", teacher.getName());

        // Validación defensiva mínima
        if (teacher == null) {
            throw new InvalidTeacherDataException("El profesor no puede ser null");
        }

        try {
            // Convertir de dominio a JPA
            TeacherJpa jpaEntity = mapper.toJpa(teacher);

            // Persistir en base de datos
            TeacherJpa savedEntity = jpaRepository.save(jpaEntity);

            log.info("Profesor creado exitosamente con ID: {}", savedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(savedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al crear profesor: {}", e.getMessage());

            // Intentar determinar si es un error de constraint único
            if (e.getMessage() != null && e.getMessage().contains("uk_teacher_email")) {
                throw new DuplicateTeacherException(
                        String.format("Ya existe un profesor con el email '%s'", teacher.getEmail())
                );
            }

            throw new InvalidTeacherDataException("Error de integridad en los datos");
        }
    }
}