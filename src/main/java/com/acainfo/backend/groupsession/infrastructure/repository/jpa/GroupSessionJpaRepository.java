package com.acainfo.backend.groupsession.infrastructure.repository.jpa;

import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.entity.GroupSessionJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface GroupSessionJpaRepository extends JpaRepository<GroupSessionJpa, Long> {

    /**
     * Busca sesiones por grupo
     */
    List<GroupSessionJpa> findByGroupId(Long groupId);

    /**
     * Busca sesiones por día de la semana
     */
    List<GroupSessionJpa> findByDayOfWeek(DayOfWeek dayOfWeek);

    /**
     * Busca sesiones por aula
     */
    List<GroupSessionJpa> findByClassroom(Classroom classroom);

    /**
     * Busca sesiones por tipo
     */
    List<GroupSessionJpa> findByType(SessionType type);

    /**
     * Busca sesiones por grupo y día
     */
    List<GroupSessionJpa> findByGroupIdAndDayOfWeek(Long groupId, DayOfWeek dayOfWeek);

    /**
     * Busca sesiones por aula y día
     */
    List<GroupSessionJpa> findByClassroomAndDayOfWeek(Classroom classroom, DayOfWeek dayOfWeek);

    /**
     * Busca sesiones por grupo ordenadas por día y hora
     */
    List<GroupSessionJpa> findByGroupIdOrderByDayOfWeekAscStartTimeAsc(Long groupId);

    /**
     * Cuenta sesiones por grupo
     */
    long countByGroupId(Long groupId);

    /**
     * Elimina todas las sesiones de un grupo
     */
    void deleteByGroupId(Long groupId);
}