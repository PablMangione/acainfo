package com.acainfo.backend.groupsession.application;

import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;
import com.acainfo.backend.groupsession.infrastructure.controller.dto.GroupSessionOutputDto;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Caso de uso para la lectura y consulta de sesiones de grupo.
 * Define el contrato para las operaciones de consulta de sesiones.
 */
public interface ReadGroupSessionUseCase {

    /**
     * Obtiene una sesión por su ID.
     *
     * @param id el identificador de la sesión
     * @return la sesión encontrada
     * @throws com.acainfo.backend.groupsession.domain.exception.GroupSessionNotFoundException
     *         si no se encuentra la sesión
     */
    GroupSessionOutputDto findById(Long id);

    /**
     * Obtiene todas las sesiones del sistema.
     *
     * @return lista de todas las sesiones
     */
    List<GroupSessionOutputDto> findAll();

    /**
     * Obtiene las sesiones de un grupo específico.
     *
     * @param groupId el identificador del grupo
     * @return lista de sesiones del grupo ordenadas por día y hora
     */
    List<GroupSessionOutputDto> findByGroup(Long groupId);

    /**
     * Obtiene las sesiones de un día específico.
     *
     * @param dayOfWeek el día de la semana
     * @return lista de sesiones del día
     */
    List<GroupSessionOutputDto> findByDayOfWeek(DayOfWeek dayOfWeek);

    /**
     * Obtiene las sesiones de un aula específica.
     *
     * @param classroom el aula
     * @return lista de sesiones del aula
     */
    List<GroupSessionOutputDto> findByClassroom(Classroom classroom);

    /**
     * Obtiene las sesiones por tipo.
     *
     * @param type el tipo de sesión
     * @return lista de sesiones del tipo especificado
     */
    List<GroupSessionOutputDto> findByType(SessionType type);

    /**
     * Obtiene el horario semanal de un grupo.
     * Las sesiones vienen ordenadas por día y hora.
     *
     * @param groupId el identificador del grupo
     * @return lista de sesiones ordenadas
     */
    List<GroupSessionOutputDto> getWeeklyScheduleForGroup(Long groupId);

    /**
     * Obtiene el horario de un aula para un día específico.
     *
     * @param classroom el aula
     * @param dayOfWeek el día de la semana
     * @return lista de sesiones del aula en ese día
     */
    List<GroupSessionOutputDto> getClassroomSchedule(Classroom classroom, DayOfWeek dayOfWeek);

    /**
     * Verifica si existe una sesión con el ID especificado.
     *
     * @param id el identificador a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Long id);
}