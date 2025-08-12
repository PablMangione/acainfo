package com.acainfo.backend.subject.application;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectOutputDto;

import java.util.List;

/**
 * Caso de uso para la lectura y consulta de asignaturas.
 * Define el contrato para las operaciones de consulta de asignaturas.
 */
public interface ReadSubjectUseCase {

    /**
     * Obtiene una asignatura por su ID.
     *
     * @param id el identificador de la asignatura
     * @return la asignatura encontrada
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura
     */
    SubjectOutputDto findById(long id);

    /**
     * Obtiene todas las asignaturas del sistema.
     *
     * @return lista de todas las asignaturas
     */
    List<SubjectOutputDto> findAll();

    /**
     * Obtiene solo las asignaturas activas.
     *
     * @return lista de asignaturas activas
     */
    List<SubjectOutputDto> findAllActive();

    /**
     * Busca asignaturas por carrera.
     *
     * @param major la carrera a filtrar
     * @return lista de asignaturas de la carrera especificada
     */
    List<SubjectOutputDto> findByMajor(Major major);

    /**
     * Busca asignaturas activas por carrera.
     *
     * @param major la carrera a filtrar
     * @return lista de asignaturas activas de la carrera especificada
     */
    List<SubjectOutputDto> findByMajorActive(Major major);

    /**
     * Busca asignaturas por carrera y año.
     *
     * @param major la carrera a filtrar
     * @param courseYear el año del curso
     * @return lista de asignaturas filtradas
     */
    List<SubjectOutputDto> findByMajorAndCourseYear(Major major, CourseYear courseYear);

    /**
     * Busca asignaturas por carrera, año y cuatrimestre.
     *
     * @param major la carrera a filtrar
     * @param courseYear el año del curso
     * @param quarter el cuatrimestre
     * @return lista de asignaturas filtradas
     */
    List<SubjectOutputDto> findByMajorAndCourseYearAndQuarter(
            Major major,
            CourseYear courseYear,
            Quarter quarter
    );

    /**
     * Busca asignaturas por nombre (búsqueda parcial, case-insensitive).
     *
     * @param name el texto a buscar en el nombre
     * @return lista de asignaturas que contienen el texto en su nombre
     */
    List<SubjectOutputDto> searchByName(String name);

    /**
     * Verifica si existe una asignatura con el ID especificado.
     *
     * @param id el identificador a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(long id);
}