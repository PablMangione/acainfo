package com.acainfo.backend.subject.domain.repository;

import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.globalenum.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de lectura para la entidad Subject.
 *
 * Contiene operaciones de consulta sin efectos secundarios.
 * Los filtrados complejos y validaciones se manejan en la capa de servicio.
 */
public interface ReadSubjectRepository {

    /**
     * Busca una asignatura por su ID.
     */
    Optional<Subject> findById(Long id);

    /**
     * Obtiene todas las asignaturas.
     */
    List<Subject> findAll();

    /**
     * Obtiene asignaturas paginadas.
     *
     * @param pageable información de paginación y ordenamiento
     * @return página de asignaturas
     */
    Page<Subject> findAll(Pageable pageable);

    /**
     * Verifica si existe una asignatura con el ID dado.
     */
    boolean existsById(Long id);

    /**
     * Busca asignaturas por carrera.
     */
    List<Subject> findByMajor(Major major);

    /**
     * Busca asignaturas por carrera y año.
     */
    List<Subject> findByMajorAndCourseYear(Major major, CourseYear courseYear);

    /**
     * Busca asignaturas por carrera, año y cuatrimestre.
     */
    List<Subject> findByMajorAndCourseYearAndQuarter(
            Major major,
            CourseYear courseYear,
            Quarter quarter
    );

    /**
     * Búsqueda por texto en el nombre (case-insensitive).
     */
    List<Subject> findByNameContainingIgnoreCase(String name);
}