package com.acainfo.backend.auth.application;

/**
 * Caso de uso para cerrar sesión.
 * Invalida los tokens del usuario actual.
 */
public interface LogoutUseCase {

    /**
     * Cierra la sesión del usuario actual.
     * Añade el token a la blacklist para invalidarlo.
     *
     * @param token el token JWT a invalidar
     */
    void logout(String token);
}