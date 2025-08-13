package com.acainfo.backend.auth.infrastructure.security;

import com.acainfo.backend.auth.domain.exception.InvalidTokenException;
import com.acainfo.backend.auth.domain.model.AuthUser;
import com.acainfo.backend.auth.domain.model.UserRole;
import com.acainfo.backend.auth.domain.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación del proveedor de tokens JWT.
 * Gestiona la creación, validación y blacklist de tokens.
 */
@Component
@Slf4j
public class JwtTokenProvider implements TokenService {

    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiration:900000}") // 15 minutos por defecto
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration:604800000}") // 7 días por defecto
    private long refreshTokenExpiration;

    // Blacklist de tokens en memoria (en producción usar Redis)
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public String generateAccessToken(AuthUser user) {
        return generateToken(user, accessTokenExpiration, TokenType.ACCESS);
    }

    @Override
    public String generateRefreshToken(AuthUser user) {
        return generateToken(user, refreshTokenExpiration, TokenType.REFRESH);
    }

    private String generateToken(AuthUser user, long expiration, TokenType tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("type", tokenType.name());
        claims.put("roles", user.getRoles().stream()
                .map(UserRole::name)
                .collect(Collectors.toList()));

        return createToken(claims, user.getId(), expiration);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Optional<String> validateTokenAndGetUserId(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                log.warn("Token está en la lista negra");
                return Optional.empty();
            }

            Claims claims = extractAllClaims(token);
            String userId = claims.getSubject();

            return Optional.ofNullable(userId);
        } catch (ExpiredJwtException e) {
            log.error("Token expirado: {}", e.getMessage());
            throw new InvalidTokenException("El token ha expirado");
        } catch (JwtException e) {
            log.error("Token inválido: {}", e.getMessage());
            throw new InvalidTokenException("Token inválido");
        }
    }

    @Override
    public TokenType getTokenType(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String type = claims.get("type", String.class);
            return TokenType.valueOf(type);
        } catch (Exception e) {
            return TokenType.INVALID;
        }
    }

    @Override
    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
        log.info("Token añadido a la lista negra");
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * Extrae el email del token
     */
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("email", String.class);
    }

    /**
     * Extrae los roles del token
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    /**
     * Verifica si el token ha expirado
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Extrae todos los claims del token
     */
    private Claims extractAllClaims(String token) {
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

    /**
     * Limpia tokens expirados de la blacklist (método para ejecutar periódicamente)
     */
    public void cleanupBlacklist() {
        // En producción, esto debería manejarse con TTL en Redis
        log.info("Limpieza de blacklist iniciada");
        // Por ahora, simplemente limpiamos toda la lista cada 24 horas
        blacklistedTokens.clear();
    }
}