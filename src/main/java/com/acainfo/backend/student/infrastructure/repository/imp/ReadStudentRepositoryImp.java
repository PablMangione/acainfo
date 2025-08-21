package com.acainfo.backend.student.infrastructure.repository.imp;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.domain.repository.ReadStudentRepository;
import com.acainfo.backend.student.infrastructure.repository.jpa.StudentJpaRepository;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;
import com.acainfo.backend.student.infrastructure.repository.mapper.StudentJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de lectura de estudiantes.
 * Adapta las consultas del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadStudentRepositoryImp implements ReadStudentRepository {

    private final StudentJpaRepository jpaRepository;
    private final StudentJpaMapper mapper;

    @Override
    public Optional<Student> findById(Long id) {
        log.debug("Buscando estudiante por ID: {}", id);

        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Student> findAll() {
        log.debug("Obteniendo todos los estudiantes");

        List<StudentJpa> jpaEntities = jpaRepository.findAll();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de estudiante con ID: {}", id);

        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        log.debug("Buscando estudiante por email: {}", email);

        // Validación defensiva mínima
        if (email == null) {
            log.warn("Búsqueda con email null");
            return Optional.empty();
        }

        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Verificando existencia de estudiante con email: {}", email);

        if (email == null) {
            return false;
        }

        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<Student> findByMajor(Major major) {
        log.debug("Buscando estudiantes por carrera: {}", major);

        List<StudentJpa> jpaEntities = jpaRepository.findByMajor(major);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Student> findByIsActiveTrue() {
        log.debug("Obteniendo estudiantes activos");

        List<StudentJpa> jpaEntities = jpaRepository.findByIsActiveTrue();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Student> findByMajorAndIsActiveTrue(Major major) {
        log.debug("Obteniendo estudiantes activos por carrera: {}", major);

        List<StudentJpa> jpaEntities = jpaRepository.findByMajorAndIsActiveTrue(major);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Student> findByNameContainingIgnoreCase(String name) {
        log.debug("Buscando estudiantes por nombre que contenga: {}", name);

        // Validación defensiva mínima
        if (name == null) {
            log.warn("Búsqueda con nombre null, retornando lista vacía");
            return List.of();
        }

        List<StudentJpa> jpaEntities = jpaRepository.findByNameContainingIgnoreCase(name);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Student> findByLastNameContainingIgnoreCase(String lastName) {
        log.debug("Buscando estudiantes por apellido que contenga: {}", lastName);

        // Validación defensiva mínima
        if (lastName == null) {
            log.warn("Búsqueda con apellido null, retornando lista vacía");
            return List.of();
        }

        List<StudentJpa> jpaEntities = jpaRepository.findByLastNameContainingIgnoreCase(lastName);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Student> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String name, String lastName) {
        log.debug("Buscando estudiantes por nombre o apellido que contenga: {} o {}", name, lastName);

        // Si ambos son null, retornar lista vacía
        if (name == null && lastName == null) {
            log.warn("Búsqueda con nombre y apellido null, retornando lista vacía");
            return List.of();
        }

        // Si uno es null, usar el otro para ambos parámetros
        String searchName = name != null ? name : lastName;
        String searchLastName = lastName != null ? lastName : name;

        List<StudentJpa> jpaEntities = jpaRepository
                .findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchName, searchLastName);
        return mapper.toDomainList(jpaEntities);
    }
}