package com.acainfo.backend.subjectgroup.application;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupOutputDto;

import java.util.List;

/**
 * Caso de uso para la lectura y consulta de grupos de asignatura.
 * Define el contrato para las operaciones de consulta de grupos.
 */
public interface ReadSubjectGroupUseCase {

    /**
     * Obtiene un grupo por su ID.
     *
     * @param id el identificador del grupo
     * @return el grupo encontrado
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     */
    SubjectGroupOutputDto findById(Long id);

    /**
     * Obtiene todos los grupos del sistema.
     *
     * @return lista de todos los grupos
     */
    List<SubjectGroupOutputDto> findAll();

    /**
     * Obtiene los grupos de una asignatura específica.
     *
     * @param subjectId el identificador de la asignatura
     * @return lista de grupos de la asignatura
     */
    List<SubjectGroupOutputDto> findBySubject(Long subjectId);

    /**
     * Obtiene los grupos de un profesor específico.
     *
     * @param teacherId el identificador del profesor
     * @return lista de grupos del profesor
     */
    List<SubjectGroupOutputDto> findByTeacher(Long teacherId);

    /**
     * Obtiene los grupos por estado.
     *
     * @param status el estado a filtrar
     * @return lista de grupos con el estado especificado
     */
    List<SubjectGroupOutputDto> findByStatus(GroupStatus status);

    /**
     * Obtiene los grupos por tipo.
     *
     * @param type el tipo de grupo
     * @return lista de grupos del tipo especificado
     */
    List<SubjectGroupOutputDto> findByType(GroupType type);

    /**
     * Obtiene los grupos activos de una asignatura.
     *
     * @param subjectId el identificador de la asignatura
     * @return lista de grupos activos de la asignatura
     */
    List<SubjectGroupOutputDto> findActiveGroupsBySubject(Long subjectId);

    /**
     * Obtiene los grupos con plazas disponibles para una asignatura.
     * Útil para que los estudiantes vean dónde pueden inscribirse.
     *
     * @param subjectId el identificador de la asignatura
     * @return lista de grupos con plazas disponibles
     */
    List<SubjectGroupOutputDto> findAvailableGroupsBySubject(Long subjectId);

    /**
     * Obtiene los grupos activos de un profesor.
     *
     * @param teacherId el identificador del profesor
     * @return lista de grupos activos del profesor
     */
    List<SubjectGroupOutputDto> findActiveGroupsByTeacher(Long teacherId);

    /**
     * Verifica si existe un grupo con el ID especificado.
     *
     * @param id el identificador a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Long id);

    /**
     * Verifica si un grupo tiene plazas disponibles.
     *
     * @param groupId el identificador del grupo
     * @return true si hay plazas, false si está completo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     */
    boolean hasAvailableSpots(Long groupId);

    /**
     * Obtiene el número de grupos activos de un profesor.
     * Útil para estadísticas y límites.
     *
     * @param teacherId el identificador del profesor
     * @return número de grupos activos
     */
    long countActiveGroupsByTeacher(Long teacherId);
}