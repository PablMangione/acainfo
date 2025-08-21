package com.acainfo.backend.auth.application;

import com.acainfo.backend.auth.infrastructure.controller.dto.TokenValidationResponseDto;

/**
 * Caso de uso para validar tokens JWT.
 * Útil para verificar si un token sigue siendo válido.
 */
public interface ValidateTokenUseCase {

    /**
     * Valida un token JWT y retorna información sobre el mismo.
     *
     * @param token el token a validar
     * @return información sobre la validez del token
     */
    TokenValidationResponseDto validate(String token);
}