package com.acainfo.backend.subjectgroup.infrastructure.repository.imp;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.domain.repository.ReadSubjectGroupRepository;
import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.SubjectGroupJpaRepository;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import com.acainfo.backend.subjectgroup.infrastructure.repository.mapper.SubjectGroupJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de lectura de grupos de asignatura.
 * Adapta las consultas del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadSubjectGroupRepositoryImp implements ReadSubjectGroupRepository {

    private final SubjectGroupJpaRepository jpaRepository;
    private final SubjectGroupJpaMapper mapper;

    @Override
    public Optional<SubjectGroup> findById(Long id) {
        log.debug("Buscando grupo por ID: {}", id);

        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<SubjectGroup> findAll() {
        log.debug("Obteniendo todos los grupos");

        List<SubjectGroupJpa> jpaEntities = jpaRepository.findAll();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de grupo con ID: {}", id);

        return jpaRepository.existsById(id);
    }

    @Override
    public List<SubjectGroup> findBySubjectId(Long subjectId) {
        log.debug("Buscando grupos por asignatura ID: {}", subjectId);

        List<SubjectGroupJpa> jpaEntities = jpaRepository.findBySubjectId(subjectId);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<SubjectGroup> findByTeacherId(Long teacherId) {
        log.debug("Buscando grupos por profesor ID: {}", teacherId);

        List<SubjectGroupJpa> jpaEntities = jpaRepository.findByTeacherId(teacherId);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<SubjectGroup> findByStatus(GroupStatus status) {
        log.debug("Buscando grupos por estado: {}", status);

        if (status == null) {
            log.warn("Búsqueda con estado null, retornando lista vacía");
            return List.of();
        }

        List<SubjectGroupJpa> jpaEntities = jpaRepository.findByStatus(status);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<SubjectGroup> findByType(GroupType type) {
        log.debug("Buscando grupos por tipo: {}", type);

        if (type == null) {
            log.warn("Búsqueda con tipo null, retornando lista vacía");
            return List.of();
        }

        List<SubjectGroupJpa> jpaEntities = jpaRepository.findByType(type);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<SubjectGroup> findBySubjectIdAndStatus(Long subjectId, GroupStatus status) {
        log.debug("Buscando grupos por asignatura ID: {} y estado: {}", subjectId, status);

        if (status == null) {
            log.warn("Estado null, delegando a búsqueda por asignatura");
            return findBySubjectId(subjectId);
        }

        List<SubjectGroupJpa> jpaEntities = jpaRepository.findBySubjectIdAndStatus(subjectId, status);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<SubjectGroup> findByTeacherIdAndStatus(Long teacherId, GroupStatus status) {
        log.debug("Buscando grupos por profesor ID: {} y estado: {}", teacherId, status);

        if (status == null) {
            log.warn("Estado null, delegando a búsqueda por profesor");
            return findByTeacherId(teacherId);
        }

        List<SubjectGroupJpa> jpaEntities = jpaRepository.findByTeacherIdAndStatus(teacherId, status);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<SubjectGroup> findAvailableGroupsBySubjectId(Long subjectId) {
        log.debug("Buscando grupos con plazas disponibles para asignatura ID: {}", subjectId);

        List<SubjectGroupJpa> jpaEntities = jpaRepository.findAvailableGroupsBySubjectId(subjectId);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public long countByTeacherIdAndStatus(Long teacherId, GroupStatus status) {
        log.debug("Contando grupos del profesor ID: {} con estado: {}", teacherId, status);

        if (status == null) {
            log.warn("Estado null, retornando 0");
            return 0;
        }

        return jpaRepository.countByTeacherIdAndStatus(teacherId, status);
    }
}