package com.acainfo.backend.groupsession.domain.repository;

import com.acainfo.backend.groupsession.domain.entity.GroupSession;
import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de lectura para la entidad GroupSession.
 *
 * Contiene operaciones de consulta sin efectos secundarios.
 * Los filtrados complejos y validaciones se manejan en la capa de servicio.
 */
public interface ReadGroupSessionRepository {

    /**
     * Busca una sesión por su ID.
     */
    Optional<GroupSession> findById(Long id);

    /**
     * Obtiene todas las sesiones.
     */
    List<GroupSession> findAll();

    /**
     * Verifica si existe una sesión con el ID dado.
     */
    boolean existsById(Long id);

    /**
     * Busca sesiones por grupo.
     */
    List<GroupSession> findByGroupId(Long groupId);

    /**
     * Busca sesiones por día de la semana.
     */
    List<GroupSession> findByDayOfWeek(DayOfWeek dayOfWeek);

    /**
     * Busca sesiones por aula.
     */
    List<GroupSession> findByClassroom(Classroom classroom);

    /**
     * Busca sesiones por tipo.
     */
    List<GroupSession> findByType(SessionType type);

    /**
     * Busca sesiones por grupo y día.
     */
    List<GroupSession> findByGroupIdAndDayOfWeek(Long groupId, DayOfWeek dayOfWeek);

    /**
     * Busca sesiones por aula y día.
     */
    List<GroupSession> findByClassroomAndDayOfWeek(Classroom classroom, DayOfWeek dayOfWeek);

    /**
     * Busca sesiones de un grupo ordenadas por día y hora.
     */
    List<GroupSession> findByGroupIdOrderedBySchedule(Long groupId);

    /**
     * Cuenta el número de sesiones de un grupo.
     */
    long countByGroupId(Long groupId);
}