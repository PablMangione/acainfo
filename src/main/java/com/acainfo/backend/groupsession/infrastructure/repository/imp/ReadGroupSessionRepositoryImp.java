package com.acainfo.backend.groupsession.infrastructure.repository.imp;

import com.acainfo.backend.groupsession.domain.entity.GroupSession;
import com.acainfo.backend.groupsession.domain.repository.ReadGroupSessionRepository;
import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.GroupSessionJpaRepository;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.entity.GroupSessionJpa;
import com.acainfo.backend.groupsession.infrastructure.repository.mapper.GroupSessionJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de lectura de sesiones de grupo.
 * Adapta las consultas del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadGroupSessionRepositoryImp implements ReadGroupSessionRepository {

    private final GroupSessionJpaRepository jpaRepository;
    private final GroupSessionJpaMapper mapper;

    @Override
    public Optional<GroupSession> findById(Long id) {
        log.debug("Buscando sesión por ID: {}", id);

        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<GroupSession> findAll() {
        log.debug("Obteniendo todas las sesiones");

        List<GroupSessionJpa> jpaEntities = jpaRepository.findAll();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de sesión con ID: {}", id);

        return jpaRepository.existsById(id);
    }

    @Override
    public List<GroupSession> findByGroupId(Long groupId) {
        log.debug("Buscando sesiones por grupo ID: {}", groupId);

        List<GroupSessionJpa> jpaEntities = jpaRepository.findByGroupId(groupId);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<GroupSession> findByDayOfWeek(DayOfWeek dayOfWeek) {
        log.debug("Buscando sesiones por día: {}", dayOfWeek);

        if (dayOfWeek == null) {
            log.warn("Búsqueda con día null, retornando lista vacía");
            return List.of();
        }

        List<GroupSessionJpa> jpaEntities = jpaRepository.findByDayOfWeek(dayOfWeek);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<GroupSession> findByClassroom(Classroom classroom) {
        log.debug("Buscando sesiones por aula: {}", classroom);

        if (classroom == null) {
            log.warn("Búsqueda con aula null, retornando lista vacía");
            return List.of();
        }

        List<GroupSessionJpa> jpaEntities = jpaRepository.findByClassroom(classroom);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<GroupSession> findByType(SessionType type) {
        log.debug("Buscando sesiones por tipo: {}", type);

        if (type == null) {
            log.warn("Búsqueda con tipo null, retornando lista vacía");
            return List.of();
        }

        List<GroupSessionJpa> jpaEntities = jpaRepository.findByType(type);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<GroupSession> findByGroupIdAndDayOfWeek(Long groupId, DayOfWeek dayOfWeek) {
        log.debug("Buscando sesiones por grupo ID: {} y día: {}", groupId, dayOfWeek);

        if (dayOfWeek == null) {
            log.warn("Día null, delegando a búsqueda por grupo");
            return findByGroupId(groupId);
        }

        List<GroupSessionJpa> jpaEntities = jpaRepository.findByGroupIdAndDayOfWeek(groupId, dayOfWeek);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<GroupSession> findByClassroomAndDayOfWeek(Classroom classroom, DayOfWeek dayOfWeek) {
        log.debug("Buscando sesiones por aula: {} y día: {}", classroom, dayOfWeek);

        if (classroom == null || dayOfWeek == null) {
            log.warn("Parámetros null, retornando lista vacía");
            return List.of();
        }

        List<GroupSessionJpa> jpaEntities = jpaRepository.findByClassroomAndDayOfWeek(classroom, dayOfWeek);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<GroupSession> findByGroupIdOrderedBySchedule(Long groupId) {
        log.debug("Buscando sesiones ordenadas por horario para grupo ID: {}", groupId);

        List<GroupSessionJpa> jpaEntities = jpaRepository.findByGroupIdOrderByDayOfWeekAscStartTimeAsc(groupId);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public long countByGroupId(Long groupId) {
        log.debug("Contando sesiones del grupo ID: {}", groupId);

        return jpaRepository.countByGroupId(groupId);
    }
}