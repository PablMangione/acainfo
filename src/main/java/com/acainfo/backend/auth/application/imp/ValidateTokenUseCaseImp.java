package com.acainfo.backend.auth.application.imp;

import com.acainfo.backend.auth.application.ValidateTokenUseCase;
import com.acainfo.backend.auth.domain.service.TokenService;
import com.acainfo.backend.auth.infrastructure.controller.dto.TokenValidationResponseDto;
import com.acainfo.backend.auth.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del caso de uso de validación de tokens.
 * Útil para verificar si un token sigue siendo válido.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateTokenUseCaseImp implements ValidateTokenUseCase {

    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    @Override
    public TokenValidationResponseDto validate(String token) {
        log.debug("Validando token");

        try {
            // Verificar si está en la blacklist
            if (tokenService.isTokenBlacklisted(token)) {
                log.warn("Token en blacklist");
                return buildInvalidResponse("Token invalidado (logout)");
            }

            // Validar el token y obtener el userId
            Optional<String> userIdOpt = tokenService.validateTokenAndGetUserId(token);

            if (userIdOpt.isEmpty()) {
                return buildInvalidResponse("Token inválido");
            }

            // Extraer información del token
            Claims claims = extractClaims(token);
            String userId = userIdOpt.get();
            String email = jwtTokenProvider.extractEmail(token);
            List<String> roles = jwtTokenProvider.extractRoles(token);

            // Calcular tiempo restante
            Date expiration = claims.getExpiration();
            long remainingTime = (expiration.getTime() - System.currentTimeMillis()) / 1000;

            log.debug("Token válido para usuario: {}", userId);

            return TokenValidationResponseDto.builder()
                    .valid(true)
                    .userId(userId)
                    .email(email)
                    .roles(roles)
                    .remainingTime(remainingTime > 0 ? remainingTime : 0)
                    .error(null)
                    .build();

        } catch (ExpiredJwtException e) {
            log.warn("Token expirado");
            return buildInvalidResponse("Token expirado");
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return buildInvalidResponse("Error al validar token: " + e.getMessage());
        }
    }

    /**
     * Construye una respuesta de token inválido
     */
    private TokenValidationResponseDto buildInvalidResponse(String error) {
        return TokenValidationResponseDto.builder()
                .valid(false)
                .userId(null)
                .email(null)
                .roles(null)
                .remainingTime(0L)
                .error(error)
                .build();
    }

    /**
     * Extrae los claims del token
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtiene la clave de firma
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}