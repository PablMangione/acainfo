package com.acainfo.backend.auth.application;

import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherInputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherOutputDto;

/**
 * Caso de uso para el registro de nuevos profesores.
 * Solo puede ser ejecutado por un administrador.
 */
public interface RegisterTeacherUseCase {

    /**
     * Registra un nuevo profesor en el sistema.
     * Requiere permisos de administrador.
     *
     * @param registerRequest datos de registro del profesor
     * @return datos del profesor creado
     * @throws com.acainfo.backend.teacher.domain.exception.DuplicateTeacherException
     *         si ya existe un profesor con el mismo email
     * @throws com.acainfo.backend.teacher.domain.exception.InvalidTeacherDataException
     *         si los datos proporcionados no son válidos
     * @throws com.acainfo.backend.auth.domain.exception.AuthenticationException
     *         si el usuario actual no tiene permisos de administrador
     */
    TeacherOutputDto register(TeacherInputDto registerRequest);
}