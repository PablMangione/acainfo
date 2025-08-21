package com.acainfo.backend.teacher.infrastructure.repository.imp;

import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.domain.repository.ReadTeacherRepository;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.TeacherJpaRepository;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.entity.TeacherJpa;
import com.acainfo.backend.teacher.infrastructure.repository.mapper.TeacherJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de lectura de profesores.
 * Adapta las consultas del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadTeacherRepositoryImp implements ReadTeacherRepository {

    private final TeacherJpaRepository jpaRepository;
    private final TeacherJpaMapper mapper;

    @Override
    public Optional<Teacher> findById(Long id) {
        log.debug("Buscando profesor por ID: {}", id);

        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Teacher> findAll() {
        log.debug("Obteniendo todos los profesores");

        List<TeacherJpa> jpaEntities = jpaRepository.findAll();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de profesor con ID: {}", id);

        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Teacher> findByEmail(String email) {
        log.debug("Buscando profesor por email: {}", email);

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
        log.debug("Verificando existencia de profesor con email: {}", email);

        if (email == null) {
            return false;
        }

        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<Teacher> findByIsAdminTrue() {
        log.debug("Obteniendo profesores administradores");

        List<TeacherJpa> jpaEntities = jpaRepository.findByIsAdminTrue();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Teacher> findByIsAdminFalse() {
        log.debug("Obteniendo profesores no administradores");

        List<TeacherJpa> jpaEntities = jpaRepository.findByIsAdminFalse();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Teacher> findByNameContainingIgnoreCase(String name) {
        log.debug("Buscando profesores por nombre que contenga: {}", name);

        // Validación defensiva mínima
        if (name == null) {
            log.warn("Búsqueda con nombre null, retornando lista vacía");
            return List.of();
        }

        List<TeacherJpa> jpaEntities = jpaRepository.findByNameContainingIgnoreCase(name);
        return mapper.toDomainList(jpaEntities);
    }
}