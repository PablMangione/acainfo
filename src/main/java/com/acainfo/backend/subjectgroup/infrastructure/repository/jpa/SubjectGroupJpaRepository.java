package com.acainfo.backend.subjectgroup.infrastructure.repository.jpa;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectGroupJpaRepository extends JpaRepository<SubjectGroupJpa, Long> {

    /**
     * Busca grupos por asignatura
     */
    List<SubjectGroupJpa> findBySubjectId(Long subjectId);

    /**
     * Busca grupos por profesor
     */
    List<SubjectGroupJpa> findByTeacherId(Long teacherId);

    /**
     * Busca grupos por estado
     */
    List<SubjectGroupJpa> findByStatus(GroupStatus status);

    /**
     * Busca grupos por tipo
     */
    List<SubjectGroupJpa> findByType(GroupType type);

    /**
     * Busca grupos por asignatura y estado
     */
    List<SubjectGroupJpa> findBySubjectIdAndStatus(Long subjectId, GroupStatus status);

    /**
     * Busca grupos por profesor y estado
     */
    List<SubjectGroupJpa> findByTeacherIdAndStatus(Long teacherId, GroupStatus status);

    /**
     * Busca grupos con plazas disponibles para una asignatura
     */
    @Query("SELECT g FROM SubjectGroupJpa g WHERE g.subject.id = :subjectId " +
            "AND g.status = 'ACTIVE' AND g.currentEnrollments < g.maxCapacity")
    List<SubjectGroupJpa> findAvailableGroupsBySubjectId(@Param("subjectId") Long subjectId);

    /**
     * Cuenta grupos por profesor y estado
     */
    long countByTeacherIdAndStatus(Long teacherId, GroupStatus status);

    /**
     * Verifica si existe un grupo con el nombre dado
     */
    boolean existsByName(String name);

    /**
     * Incrementa el contador de inscripciones
     */
    @Modifying
    @Query("UPDATE SubjectGroupJpa g SET g.currentEnrollments = g.currentEnrollments + 1 " +
            "WHERE g.id = :groupId AND g.currentEnrollments < g.maxCapacity")
    int incrementEnrollmentCount(@Param("groupId") Long groupId);

    /**
     * Decrementa el contador de inscripciones
     */
    @Modifying
    @Query("UPDATE SubjectGroupJpa g SET g.currentEnrollments = g.currentEnrollments - 1 " +
            "WHERE g.id = :groupId AND g.currentEnrollments > 0")
    int decrementEnrollmentCount(@Param("groupId") Long groupId);

    /**
     * Verifica si un grupo tiene inscripciones
     */
    @Query("SELECT CASE WHEN g.currentEnrollments > 0 THEN true ELSE false END " +
            "FROM SubjectGroupJpa g WHERE g.id = :groupId")
    boolean hasEnrollments(@Param("groupId") Long groupId);
}