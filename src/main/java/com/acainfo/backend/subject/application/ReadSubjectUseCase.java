package com.acainfo.backend.subject.application;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.SubjectNotFoundException;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;

import java.util.List;
import java.util.Optional;

/**
 * Caso de uso para la lectura y consulta de asignaturas.
 *
 * Define el contrato para todas las operaciones de consulta de asignaturas.
 * Trabaja exclusivamente con entidades de dominio para mantener la independencia
 * de la capa de aplicación.
 */
public interface ReadSubjectUseCase {

    /**
     * Obtiene una asignatura por su ID.
     *
     * @param id el identificador de la asignatura
     * @return Optional con la asignatura si existe, Optional.empty() si no existe
     * @throws IllegalArgumentException si el id es null o <= 0
     */
    Optional<Subject> findById(Long id);

    /**
     * Obtiene todas las asignaturas del sistema.
     *
     * @return lista de todas las asignaturas (puede estar vacía)
     */
    List<Subject> findAll();

    /**
     * Obtiene solo las asignaturas activas.
     *
     * @return lista de asignaturas con isActive = true
     */
    List<Subject> findAllActive();

    /**
     * Busca asignaturas por carrera.
     *
     * @param major la carrera a filtrar
     * @return lista de asignaturas de la carrera especificada
     * @throws IllegalArgumentException si major es null
     */
    List<Subject> findByMajor(Major major);

    /**
     * Busca asignaturas activas por carrera.
     *
     * @param major la carrera a filtrar
     * @return lista de asignaturas activas de la carrera especificada
     * @throws IllegalArgumentException si major es null
     */
    List<Subject> findByMajorActive(Major major);

    /**
     * Busca asignaturas por carrera y año.
     *
     * @param major la carrera a filtrar
     * @param courseYear el año del curso
     * @return lista de asignaturas filtradas
     * @throws IllegalArgumentException si algún parámetro es null
     */
    List<Subject> findByMajorAndCourseYear(Major major, CourseYear courseYear);

    /**
     * Busca asignaturas por carrera, año y cuatrimestre.
     *
     * @param major la carrera a filtrar
     * @param courseYear el año del curso
     * @param quarter el cuatrimestre
     * @return lista de asignaturas filtradas
     * @throws IllegalArgumentException si algún parámetro es null
     */
    List<Subject> findByMajorAndCourseYearAndQuarter(
            Major major,
            CourseYear courseYear,
            Quarter quarter
    );

    /**
     * Busca asignaturas por nombre (búsqueda parcial, case-insensitive).
     *
     * @param name el texto a buscar en el nombre
     * @return lista de asignaturas que contienen el texto en su nombre
     * @throws IllegalArgumentException si name es null o vacío
     */
    List<Subject> searchByName(String name);

    /**
     * Verifica si existe una asignatura con el ID especificado.
     *
     * @param id el identificador a verificar
     * @return true si existe, false en caso contrario
     * @throws IllegalArgumentException si el id es null o <= 0
     */
    boolean existsById(Long id);

    /**
     * Cuenta el total de asignaturas en el sistema.
     *
     * @return número total de asignaturas
     */
    long countAll();

    /**
     * Cuenta el total de asignaturas activas.
     *
     * @return número de asignaturas con isActive = true
     */
    long countActive();

    /**
     * Obtiene asignaturas paginadas.
     * Útil para interfaces con tablas paginadas.
     *
     * @param page número de página (0-based)
     * @param size tamaño de la página
     * @return lista de asignaturas de la página solicitada
     * @throws IllegalArgumentException si page < 0 o size <= 0
     */
    List<Subject> findPaginated(int page, int size);

    /**
     * Obtiene todos los grupos de una asignatura.
     *
     * @param subjectId el ID de la asignatura
     * @return lista de grupos de la asignatura
     * @throws SubjectNotFoundException si la asignatura no existe
     */
    List<SubjectGroup> findGroupsBySubjectId(Long subjectId);

    /**
     * Obtiene grupos de una asignatura filtrados por estado.
     *
     * @param subjectId el ID de la asignatura
     * @param status estado para filtrar (opcional)
     * @return lista de grupos filtrados
     * @throws SubjectNotFoundException si la asignatura no existe
     */
    List<SubjectGroup> findGroupsBySubjectId(Long subjectId, GroupStatus status);
}