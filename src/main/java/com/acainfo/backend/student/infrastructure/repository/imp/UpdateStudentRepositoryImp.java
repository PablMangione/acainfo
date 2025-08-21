package com.acainfo.backend.student.infrastructure.repository.imp;

import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.domain.exception.DuplicateStudentException;
import com.acainfo.backend.student.domain.exception.InvalidStudentDataException;
import com.acainfo.backend.student.domain.exception.StudentNotFoundException;
import com.acainfo.backend.student.domain.repository.UpdateStudentRepository;
import com.acainfo.backend.student.infrastructure.repository.jpa.StudentJpaRepository;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;
import com.acainfo.backend.student.infrastructure.repository.mapper.StudentJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de actualización de estudiantes.
 * Adapta las operaciones de actualización del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UpdateStudentRepositoryImp implements UpdateStudentRepository {

    private final StudentJpaRepository jpaRepository;
    private final StudentJpaMapper mapper;

    /**
     * Actualiza un estudiante existente.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param student el estudiante con los datos actualizados
     * @return el estudiante actualizado
     * @throws StudentNotFoundException si el estudiante no existe
     * @throws DuplicateStudentException si la actualización viola constraints únicos
     */
    @Override
    @Transactional
    public Student update(Student student) {
        log.debug("Iniciando actualización de estudiante con ID: {}", student.getId());

        // Validación defensiva mínima
        if (student == null) {
            throw new InvalidStudentDataException("El estudiante no puede ser null");
        }

        if (student.getId() == null || student.getId() == 0L) {
            throw new InvalidStudentDataException("El ID del estudiante es requerido para actualizar");
        }

        try {
            // Buscar la entidad existente
            StudentJpa existingEntity = jpaRepository.findById(student.getId())
                    .orElseThrow(() -> {
                        log.error("No se encontró estudiante con ID: {}", student.getId());
                        return new StudentNotFoundException(
                                "No se encontró el estudiante con ID: " + student.getId()
                        );
                    });

            // Actualizar los campos usando el mapper
            mapper.updateJpaFromDomain(student, existingEntity);

            // Persistir los cambios
            StudentJpa updatedEntity = jpaRepository.save(existingEntity);

            log.info("Estudiante actualizado exitosamente. ID: {}", updatedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(updatedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al actualizar estudiante: {}", e.getMessage());

            // Intentar determinar si es un error de constraint único
            if (e.getMessage() != null && e.getMessage().contains("uk_student_email")) {
                throw new DuplicateStudentException(
                        "Ya existe otro estudiante con el email proporcionado"
                );
            }

            throw new InvalidStudentDataException("Error de integridad en los datos");
        }
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de estudiante con ID: {}", id);
        return jpaRepository.existsById(id);
    }
}