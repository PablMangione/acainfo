package com.acainfo.backend.subjectgroup.domain.repository;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de lectura para la entidad SubjectGroup.
 *
 * Contiene operaciones de consulta sin efectos secundarios.
 * Los filtrados complejos y validaciones se manejan en la capa de servicio.
 */
public interface ReadSubjectGroupRepository {

    /**
     * Busca un grupo por su ID.
     */
    Optional<SubjectGroup> findById(Long id);

    /**
     * Obtiene todos los grupos.
     */
    List<SubjectGroup> findAll();

    /**
     * Verifica si existe un grupo con el ID dado.
     */
    boolean existsById(Long id);

    /**
     * Busca grupos por asignatura.
     */
    List<SubjectGroup> findBySubjectId(Long subjectId);

    /**
     * Busca grupos por profesor.
     */
    List<SubjectGroup> findByTeacherId(Long teacherId);

    /**
     * Busca grupos por estado.
     */
    List<SubjectGroup> findByStatus(GroupStatus status);

    /**
     * Busca grupos por tipo.
     */
    List<SubjectGroup> findByType(GroupType type);

    /**
     * Busca grupos activos de una asignatura.
     */
    List<SubjectGroup> findBySubjectIdAndStatus(Long subjectId, GroupStatus status);

    /**
     * Busca grupos de un profesor con un estado específico.
     */
    List<SubjectGroup> findByTeacherIdAndStatus(Long teacherId, GroupStatus status);

    /**
     * Busca grupos con plazas disponibles para una asignatura.
     * Un grupo tiene plazas si currentEnrollments < maxCapacity
     */
    List<SubjectGroup> findAvailableGroupsBySubjectId(Long subjectId);

    /**
     * Cuenta el número de grupos activos de un profesor.
     */
    long countByTeacherIdAndStatus(Long teacherId, GroupStatus status);
}