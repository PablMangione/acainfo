package com.acainfo.backend.auth.application.imp;

import com.acainfo.backend.auth.application.RefreshTokenUseCase;
import com.acainfo.backend.auth.domain.exception.InvalidTokenException;
import com.acainfo.backend.auth.domain.model.AuthUser;
import com.acainfo.backend.auth.domain.model.UserRole;
import com.acainfo.backend.auth.domain.model.UserType;
import com.acainfo.backend.auth.domain.service.TokenService;
import com.acainfo.backend.auth.infrastructure.controller.dto.TokenRefreshDto;
import com.acainfo.backend.auth.infrastructure.controller.dto.TokenRefreshResponseDto;
import com.acainfo.backend.auth.infrastructure.security.JwtTokenProvider;
import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.domain.repository.ReadStudentRepository;
import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.domain.repository.ReadTeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementación del caso de uso de refresco de token.
 * Permite obtener un nuevo access token usando el refresh token.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RefreshTokenUseCaseImp implements RefreshTokenUseCase {

    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ReadStudentRepository studentRepository;
    private final ReadTeacherRepository teacherRepository;

    @Value("${jwt.access-token.expiration:900000}")
    private long accessTokenExpiration;

    @Override
    public TokenRefreshResponseDto refresh(TokenRefreshDto refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();
        log.debug("Procesando solicitud de refresco de token");

        // Validar el refresh token
        Optional<String> userIdOpt = tokenService.validateTokenAndGetUserId(refreshToken);
        if (userIdOpt.isEmpty()) {
            log.warn("Refresh token inválido o expirado");
            throw new InvalidTokenException("Refresh token inválido o expirado");
        }

        // Verificar que sea un refresh token y no un access token
        TokenService.TokenType tokenType = tokenService.getTokenType(refreshToken);
        if (tokenType != TokenService.TokenType.REFRESH) {
            log.warn("Se intentó usar un token de tipo {} como refresh token", tokenType);
            throw new InvalidTokenException("El token proporcionado no es un refresh token válido");
        }

        String userId = userIdOpt.get();
        log.debug("Refresh token válido para usuario: {}", userId);

        // Cargar el usuario actualizado
        AuthUser authUser = loadUserById(userId);

        // Verificar que el usuario siga activo
        if (!authUser.isActive()) {
            log.warn("Usuario inactivo intentó refrescar token: {}", userId);
            throw new InvalidTokenException("Usuario inactivo");
        }

        // Generar nuevo access token
        String newAccessToken = tokenService.generateAccessToken(authUser);
        log.info("Nuevo access token generado para usuario: {}", userId);

        // Construir respuesta
        return TokenRefreshResponseDto.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000) // Convertir a segundos
                .build();
    }

    /**
     * Carga un usuario por su ID compuesto
     */
    private AuthUser loadUserById(String userId) {
        // Parsear el ID compuesto
        String[] parts = userId.split("_");
        if (parts.length != 2) {
            throw new InvalidTokenException("ID de usuario malformado en el token");
        }

        String type = parts[0];
        Long id = Long.parseLong(parts[1]);

        return switch (type) {
            case "STUDENT" -> loadStudent(id);
            case "TEACHER" -> loadTeacher(id);
            default -> throw new InvalidTokenException("Tipo de usuario desconocido: " + type);
        };
    }

    /**
     * Carga un estudiante por ID
     */
    private AuthUser loadStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new InvalidTokenException("Estudiante no encontrado: " + id));

        return convertStudentToAuthUser(student);
    }

    /**
     * Carga un profesor por ID
     */
    private AuthUser loadTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new InvalidTokenException("Profesor no encontrado: " + id));

        return convertTeacherToAuthUser(teacher);
    }

    /**
     * Convierte un Student a AuthUser
     */
    private AuthUser convertStudentToAuthUser(Student student) {
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ROLE_STUDENT);

        return AuthUser.builder()
                .id(AuthUser.createId(UserType.STUDENT, student.getId()))
                .email(student.getEmail())
                .password(student.getPassword())
                .name(student.getName() + " " + student.getLastName())
                .userType(UserType.STUDENT)
                .roles(roles)
                .isActive(student.isActive())
                .build();
    }

    /**
     * Convierte un Teacher a AuthUser
     */
    private AuthUser convertTeacherToAuthUser(Teacher teacher) {
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ROLE_TEACHER);

        if (teacher.isAdmin()) {
            roles.add(UserRole.ROLE_ADMIN);
        }

        return AuthUser.builder()
                .id(AuthUser.createId(UserType.TEACHER, teacher.getId()))
                .email(teacher.getEmail())
                .password(teacher.getPassword())
                .name(teacher.getName())
                .userType(UserType.TEACHER)
                .roles(roles)
                .isActive(true)
                .build();
    }
}