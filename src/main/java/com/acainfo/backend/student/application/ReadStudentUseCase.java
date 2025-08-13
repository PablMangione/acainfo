package com.acainfo.backend.student.application;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentOutputDto;

import java.util.List;

/**
 * Caso de uso para la lectura y consulta de estudiantes.
 * Define el contrato para las operaciones de consulta de estudiantes.
 */
public interface ReadStudentUseCase {

    /**
     * Obtiene un estudiante por su ID.
     *
     * @param id el identificador del estudiante
     * @return el estudiante encontrado
     * @throws com.acainfo.backend.student.domain.exception.StudentNotFoundException
     *         si no se encuentra el estudiante
     */
    StudentOutputDto findById(Long id);

    /**
     * Obtiene todos los estudiantes del sistema.
     *
     * @return lista de todos los estudiantes
     */
    List<StudentOutputDto> findAll();

    /**
     * Obtiene solo los estudiantes activos.
     *
     * @return lista de estudiantes activos
     */
    List<StudentOutputDto> findAllActive();

    /**
     * Busca estudiantes por carrera.
     *
     * @param major la carrera a filtrar
     * @return lista de estudiantes de la carrera especificada
     */
    List<StudentOutputDto> findByMajor(Major major);

    /**
     * Busca estudiantes activos por carrera.
     *
     * @param major la carrera a filtrar
     * @return lista de estudiantes activos de la carrera especificada
     */
    List<StudentOutputDto> findByMajorActive(Major major);

    /**
     * Busca un estudiante por su email.
     *
     * @param email el email a buscar
     * @return el estudiante encontrado
     * @throws com.acainfo.backend.student.domain.exception.StudentNotFoundException
     *         si no se encuentra el estudiante
     */
    StudentOutputDto findByEmail(String email);

    /**
     * Busca estudiantes por nombre (búsqueda parcial, case-insensitive).
     *
     * @param name el texto a buscar en el nombre
     * @return lista de estudiantes que contienen el texto en su nombre
     */
    List<StudentOutputDto> searchByName(String name);

    /**
     * Busca estudiantes por apellido (búsqueda parcial, case-insensitive).
     *
     * @param lastName el texto a buscar en el apellido
     * @return lista de estudiantes que contienen el texto en su apellido
     */
    List<StudentOutputDto> searchByLastName(String lastName);

    /**
     * Busca estudiantes por nombre o apellido (búsqueda parcial, case-insensitive).
     *
     * @param text el texto a buscar en nombre o apellido
     * @return lista de estudiantes que contienen el texto en nombre o apellido
     */
    List<StudentOutputDto> searchByFullName(String text);

    /**
     * Verifica si existe un estudiante con el ID especificado.
     *
     * @param id el identificador a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Long id);

    /**
     * Verifica si existe un estudiante con el email especificado.
     *
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}