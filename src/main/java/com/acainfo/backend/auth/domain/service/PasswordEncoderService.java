package com.acainfo.backend.auth.domain.service;

/**
 * Servicio de dominio para codificación de contraseñas.
 * Abstrae la implementación específica del algoritmo de hashing.
 */
public interface PasswordEncoderService {

    /**
     * Codifica una contraseña en texto plano
     */
    String encode(String rawPassword);

    /**
     * Verifica si una contraseña en texto plano coincide con una codificada
     */
    boolean matches(String rawPassword, String encodedPassword);
}