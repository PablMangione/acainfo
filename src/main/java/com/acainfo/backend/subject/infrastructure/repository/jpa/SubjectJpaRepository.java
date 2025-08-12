package com.acainfo.backend.subject.infrastructure.repository.jpa;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.subject.infrastructure.repository.jpa.entity.SubjectJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectJpaRepository extends JpaRepository<SubjectJpa, Long> {
    /**
     * Busca asignaturas por carrera
     */
    List<SubjectJpa> findByMajor(Major major);

    /**
     * Busca asignaturas por carrera y año
     */
    List<SubjectJpa> findByMajorAndCourseYear(Major major, CourseYear courseYear);

    /**
     * Busca asignaturas por carrera, año y cuatrimestre
     */
    List<SubjectJpa> findByMajorAndCourseYearAndQuarter(
            Major major,
            CourseYear courseYear,
            Quarter quarter
    );

    /**
     * Búsqueda por texto en el nombre (case-insensitive)
     */
    List<SubjectJpa> findByNameContainingIgnoreCase(String name);

    /**
     * Busca solo asignaturas activas
     */
    List<SubjectJpa> findByIsActiveTrue();

    /**
     * Busca asignaturas activas por carrera
     */
    List<SubjectJpa> findByMajorAndIsActiveTrue(Major major);
}