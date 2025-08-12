package com.acainfo.backend.teacher.application;

import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherInputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherOutputDto;

/**
 * Caso de uso para la creación de profesores.
 * Define el contrato para la creación de nuevos profesores en el sistema.
 */
public interface CreateTeacherUseCase {

    /**
     * Crea un nuevo profesor en el sistema.
     *
     * @param inputDto datos del profesor a crear
     * @return el profesor creado con su ID generado
     * @throws com.acainfo.backend.teacher.domain.exception.DuplicateTeacherException
     *         si ya existe un profesor con el mismo email
     * @throws com.acainfo.backend.teacher.domain.exception.InvalidTeacherDataException
     *         si los datos proporcionados no son válidos
     */
    TeacherOutputDto create(TeacherInputDto inputDto);
}