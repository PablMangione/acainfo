package com.acainfo.backend.teacher.infrastructure.repository.imp;

import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.domain.exception.DuplicateTeacherException;
import com.acainfo.backend.teacher.domain.exception.InvalidTeacherDataException;
import com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException;
import com.acainfo.backend.teacher.domain.repository.UpdateTeacherRepository;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.TeacherJpaRepository;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.entity.TeacherJpa;
import com.acainfo.backend.teacher.infrastructure.repository.mapper.TeacherJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de actualización de profesores.
 * Adapta las operaciones de actualización del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UpdateTeacherRepositoryImp implements UpdateTeacherRepository {

    private final TeacherJpaRepository jpaRepository;
    private final TeacherJpaMapper mapper;

    /**
     * Actualiza un profesor existente.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param teacher el profesor con los datos actualizados
     * @return el profesor actualizado
     * @throws TeacherNotFoundException si el profesor no existe
     * @throws DuplicateTeacherException si la actualización viola constraints únicos
     */
    @Override
    @Transactional
    public Teacher update(Teacher teacher) {
        log.debug("Iniciando actualización de profesor con ID: {}", teacher.getId());

        // Validación defensiva mínima
        if (teacher == null) {
            throw new InvalidTeacherDataException("El profesor no puede ser null");
        }

        if (teacher.getId() == 0L) {
            throw new InvalidTeacherDataException("El ID del profesor es requerido para actualizar");
        }

        try {
            // Buscar la entidad existente
            TeacherJpa existingEntity = jpaRepository.findById(teacher.getId())
                    .orElseThrow(() -> {
                        log.error("No se encontró profesor con ID: {}", teacher.getId());
                        return new TeacherNotFoundException(
                                "No se encontró el profesor con ID: " + teacher.getId()
                        );
                    });

            // Actualizar los campos usando el mapper
            mapper.updateJpaFromDomain(teacher, existingEntity);

            // Persistir los cambios
            TeacherJpa updatedEntity = jpaRepository.save(existingEntity);

            log.info("Profesor actualizado exitosamente. ID: {}", updatedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(updatedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al actualizar profesor: {}", e.getMessage());

            // Intentar determinar si es un error de constraint único
            if (e.getMessage() != null && e.getMessage().contains("uk_teacher_email")) {
                throw new DuplicateTeacherException(
                        "Ya existe otro profesor con el email proporcionado"
                );
            }

            throw new InvalidTeacherDataException("Error de integridad en los datos");
        }
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de profesor con ID: {}", id);
        return jpaRepository.existsById(id);
    }
}