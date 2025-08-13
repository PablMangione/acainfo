package com.acainfo.backend.auth.application.imp;

import com.acainfo.backend.auth.application.LogoutUseCase;
import com.acainfo.backend.auth.domain.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Implementación del caso de uso de logout.
 * Invalida los tokens del usuario actual.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutUseCaseImp implements LogoutUseCase {

    private final TokenService tokenService;

    @Override
    public void logout(String token) {
        // Obtener el usuario actual del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication != null ? authentication.getName() : "unknown";

        log.info("Procesando logout para usuario: {}", userId);

        // Añadir el token a la blacklist
        tokenService.invalidateToken(token);

        // Limpiar el contexto de seguridad
        SecurityContextHolder.clearContext();

        log.info("Logout exitoso para usuario: {}", userId);
    }
}