package com.acainfo.backend.auth.application;

import com.acainfo.backend.auth.infrastructure.controller.dto.LoginRequestDto;
import com.acainfo.backend.auth.infrastructure.controller.dto.LoginResponseDto;

/**
 * Caso de uso para el inicio de sesión de usuarios.
 * Maneja la autenticación de estudiantes y profesores.
 */
public interface LoginUseCase {

    /**
     * Autentica un usuario con sus credenciales.
     *
     * @param loginRequest datos de login (email y contraseña)
     * @return respuesta con tokens y datos del usuario
     * @throws com.acainfo.backend.auth.domain.exception.InvalidCredentialsException
     *         si las credenciales son incorrectas
     * @throws com.acainfo.backend.auth.domain.exception.AuthenticationException
     *         si el usuario está inactivo o hay otro error de autenticación
     */
    LoginResponseDto login(LoginRequestDto loginRequest);
}