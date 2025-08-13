package com.acainfo.backend.auth.infrastructure.service;

import com.acainfo.backend.auth.domain.service.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de codificación usando BCrypt.
 * Actúa como adaptador entre el dominio y Spring Security.
 */
@Service
@RequiredArgsConstructor
public class BCryptPasswordEncoderService implements PasswordEncoderService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("La contraseña no puede ser null");
        }
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}