package com.acainfo.backend.auth.application;

import com.acainfo.backend.auth.infrastructure.controller.dto.StudentRegisterRequestDto;
import com.acainfo.backend.auth.infrastructure.controller.dto.LoginResponseDto;

/**
 * Caso de uso para el registro de nuevos estudiantes.
 * Crea el estudiante y automáticamente lo autentica.
 */
public interface RegisterStudentUseCase {

    /**
     * Registra un nuevo estudiante en el sistema.
     *
     * @param registerRequest datos de registro del estudiante
     * @return respuesta con tokens y datos del nuevo usuario
     * @throws com.acainfo.backend.student.domain.exception.DuplicateStudentException
     *         si ya existe un estudiante con el mismo email
     * @throws com.acainfo.backend.student.domain.exception.InvalidStudentDataException
     *         si los datos proporcionados no son válidos
     */
    LoginResponseDto register(StudentRegisterRequestDto registerRequest);
}