package com.acainfo.backend.teacher.application;

import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherOutputDto;

import java.util.List;

/**
 * Caso de uso para la lectura y consulta de profesores.
 * Define el contrato para las operaciones de consulta de profesores.
 */
public interface ReadTeacherUseCase {

    /**
     * Obtiene un profesor por su ID.
     *
     * @param id el identificador del profesor
     * @return el profesor encontrado
     * @throws com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException
     *         si no se encuentra el profesor
     */
    TeacherOutputDto findById(Long id);

    /**
     * Obtiene todos los profesores del sistema.
     *
     * @return lista de todos los profesores
     */
    List<TeacherOutputDto> findAll();

    /**
     * Obtiene solo los profesores administradores.
     *
     * @return lista de profesores con rol de administrador
     */
    List<TeacherOutputDto> findAllAdmins();

    /**
     * Obtiene solo los profesores no administradores.
     *
     * @return lista de profesores sin rol de administrador
     */
    List<TeacherOutputDto> findAllTeachers();

    /**
     * Busca un profesor por su email.
     *
     * @param email el email a buscar
     * @return el profesor encontrado
     * @throws com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException
     *         si no se encuentra el profesor
     */
    TeacherOutputDto findByEmail(String email);

    /**
     * Busca profesores por nombre (búsqueda parcial, case-insensitive).
     *
     * @param name el texto a buscar en el nombre
     * @return lista de profesores que contienen el texto en su nombre
     */
    List<TeacherOutputDto> searchByName(String name);

    /**
     * Verifica si existe un profesor con el ID especificado.
     *
     * @param id el identificador a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Long id);

    /**
     * Verifica si existe un profesor con el email especificado.
     *
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}