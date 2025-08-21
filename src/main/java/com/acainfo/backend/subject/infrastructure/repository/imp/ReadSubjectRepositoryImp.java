package com.acainfo.backend.subject.infrastructure.repository.imp;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.subject.infrastructure.repository.jpa.SubjectJpaRepository;
import com.acainfo.backend.subject.infrastructure.repository.jpa.entity.SubjectJpa;
import com.acainfo.backend.subject.infrastructure.repository.mapper.SubjectJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de lectura de asignaturas.
 * Adapta las consultas del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadSubjectRepositoryImp implements ReadSubjectRepository {

    private final SubjectJpaRepository jpaRepository;
    private final SubjectJpaMapper mapper;

    @Override
    public Optional<Subject> findById(Long id) {
        log.debug("Buscando asignatura por ID: {}", id);

        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Subject> findAll() {
        log.debug("Obteniendo todas las asignaturas");

        List<SubjectJpa> jpaEntities = jpaRepository.findAll();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public Page<Subject> findAll(Pageable pageable) {
        log.debug("Obteniendo asignaturas paginadas - página: {}, tamaño: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<SubjectJpa> jpaPage = jpaRepository.findAll(pageable);

        // Convertir las entidades JPA a entidades de dominio
        List<Subject> subjects = mapper.toDomainList(jpaPage.getContent());

        // Crear una nueva Page con las entidades de dominio
        return new PageImpl<>(subjects, pageable, jpaPage.getTotalElements());
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de asignatura con ID: {}", id);

        return jpaRepository.existsById(id);
    }

    @Override
    public List<Subject> findByMajor(Major major) {
        log.debug("Buscando asignaturas por carrera: {}", major);

        List<SubjectJpa> jpaEntities = jpaRepository.findByMajor(major);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Subject> findByMajorAndCourseYear(Major major, CourseYear courseYear) {
        log.debug("Buscando asignaturas por carrera: {} y año: {}", major, courseYear);

        List<SubjectJpa> jpaEntities = jpaRepository.findByMajorAndCourseYear(major, courseYear);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Subject> findByMajorAndCourseYearAndQuarter(
            Major major,
            CourseYear courseYear,
            Quarter quarter) {
        log.debug("Buscando asignaturas por carrera: {}, año: {} y cuatrimestre: {}",
                major, courseYear, quarter);

        List<SubjectJpa> jpaEntities = jpaRepository.findByMajorAndCourseYearAndQuarter(
                major, courseYear, quarter);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Subject> findByNameContainingIgnoreCase(String name) {
        log.debug("Buscando asignaturas por nombre que contenga: {}", name);

        // Validación defensiva mínima
        if (name == null) {
            log.warn("Búsqueda con nombre null, retornando lista vacía");
            return List.of();
        }

        List<SubjectJpa> jpaEntities = jpaRepository.findByNameContainingIgnoreCase(name);
        return mapper.toDomainList(jpaEntities);
    }
}