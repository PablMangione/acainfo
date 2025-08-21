package com.acainfo.backend.auth.domain.service;

import com.acainfo.backend.auth.domain.model.AuthUser;

import java.util.Optional;

/**
 * Servicio de dominio para gestión de tokens.
 * Define el contrato para la generación y validación de tokens.
 */
public interface TokenService {

    /**
     * Genera un token de acceso para un usuario
     */
    String generateAccessToken(AuthUser user);

    /**
     * Genera un token de refresco para un usuario
     */
    String generateRefreshToken(AuthUser user);

    /**
     * Valida un token y retorna el ID del usuario si es válido
     */
    Optional<String> validateTokenAndGetUserId(String token);

    /**
     * Obtiene el tipo de token
     */
    TokenType getTokenType(String token);

    /**
     * Invalida un token (para logout)
     */
    void invalidateToken(String token);

    /**
     * Verifica si un token está en la lista negra
     */
    boolean isTokenBlacklisted(String token);

    enum TokenType {
        ACCESS,
        REFRESH,
        INVALID
    }
}