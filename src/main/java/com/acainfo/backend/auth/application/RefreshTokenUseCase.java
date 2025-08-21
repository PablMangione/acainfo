package com.acainfo.backend.auth.application;

import com.acainfo.backend.auth.infrastructure.controller.dto.TokenRefreshDto;
import com.acainfo.backend.auth.infrastructure.controller.dto.TokenRefreshResponseDto;

/**
 * Caso de uso para refrescar el token de acceso.
 * Permite obtener un nuevo access token usando el refresh token.
 */
public interface RefreshTokenUseCase {

    /**
     * Refresca el token de acceso usando un refresh token válido.
     *
     * @param refreshRequest datos con el refresh token
     * @return nuevo access token
     * @throws com.acainfo.backend.auth.domain.exception.InvalidTokenException
     *         si el refresh token es inválido o ha expirado
     */
    TokenRefreshResponseDto refresh(TokenRefreshDto refreshRequest);
}