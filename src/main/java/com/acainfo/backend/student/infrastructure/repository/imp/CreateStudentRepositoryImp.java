package com.acainfo.backend.student.infrastructure.repository.imp;

import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.domain.exception.DuplicateStudentException;
import com.acainfo.backend.student.domain.exception.InvalidStudentDataException;
import com.acainfo.backend.student.domain.repository.CreateStudentRepository;
import com.acainfo.backend.student.infrastructure.repository.jpa.StudentJpaRepository;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;
import com.acainfo.backend.student.infrastructure.repository.mapper.StudentJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de creación de estudiantes.
 * Adapta la interfaz del dominio a la infraestructura de persistencia.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CreateStudentRepositoryImp implements CreateStudentRepository {

    private final StudentJpaRepository jpaRepository;
    private final StudentJpaMapper mapper;

    /**
     * Persiste un nuevo estudiante en el sistema.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param student el estudiante a crear
     * @return el estudiante creado con su ID generado
     * @throws DuplicateStudentException si ya existe un estudiante con el mismo email
     * @throws InvalidStudentDataException si ocurre un error de persistencia
     */
    @Override
    @Transactional
    public Student save(Student student) {
        log.debug("Iniciando creación de estudiante: {} {}", student.getName(), student.getLastName());

        // Validación defensiva mínima
        if (student == null) {
            throw new InvalidStudentDataException("El estudiante no puede ser null");
        }

        try {
            // Convertir de dominio a JPA
            StudentJpa jpaEntity = mapper.toJpa(student);

            // Persistir en base de datos
            StudentJpa savedEntity = jpaRepository.save(jpaEntity);

            log.info("Estudiante creado exitosamente con ID: {}", savedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(savedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al crear estudiante: {}", e.getMessage());

            // Intentar determinar si es un error de constraint único
            if (e.getMessage() != null && e.getMessage().contains("uk_student_email")) {
                throw new DuplicateStudentException(
                        String.format("Ya existe un estudiante con el email '%s'", student.getEmail())
                );
            }

            throw new InvalidStudentDataException("Error de integridad en los datos");
        }
    }
}