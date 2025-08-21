package com.acainfo.backend.auth.application.imp;

import com.acainfo.backend.auth.application.LoginUseCase;
import com.acainfo.backend.auth.domain.exception.AuthenticationException;
import com.acainfo.backend.auth.domain.exception.InvalidCredentialsException;
import com.acainfo.backend.auth.domain.model.AuthUser;
import com.acainfo.backend.auth.domain.model.UserRole;
import com.acainfo.backend.auth.domain.model.UserType;
import com.acainfo.backend.auth.domain.service.PasswordEncoderService;
import com.acainfo.backend.auth.domain.service.TokenService;
import com.acainfo.backend.auth.infrastructure.controller.dto.LoginRequestDto;
import com.acainfo.backend.auth.infrastructure.controller.dto.LoginResponseDto;
import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.domain.repository.ReadStudentRepository;
import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.domain.repository.ReadTeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso de login.
 * Maneja la autenticación de estudiantes y profesores.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LoginUseCaseImp implements LoginUseCase {

    private final ReadStudentRepository studentRepository;
    private final ReadTeacherRepository teacherRepository;
    private final PasswordEncoderService passwordEncoder;
    private final TokenService tokenService;

    @Value("${jwt.access-token.expiration:900000}")
    private long accessTokenExpiration;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        log.info("Intento de login para email: {}", loginRequest.getEmail());

        // Buscar el usuario (estudiante o profesor)
        AuthUser authUser = findUserByEmail(loginRequest.getEmail());

        // Verificar la contraseña
        if (!passwordEncoder.matches(loginRequest.getPassword(), authUser.getPassword())) {
            log.warn("Contraseña incorrecta para usuario: {}", loginRequest.getEmail());
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }

        // Verificar si el usuario está activo
        if (!authUser.isActive()) {
            log.warn("Intento de login de usuario inactivo: {}", loginRequest.getEmail());
            throw new AuthenticationException("Tu cuenta está inactiva. Contacta con el administrador.");
        }

        // Generar tokens
        String accessToken = tokenService.generateAccessToken(authUser);
        String refreshToken = tokenService.generateRefreshToken(authUser);

        log.info("Login exitoso para usuario: {} ({})", authUser.getId(), authUser.getUserType());

        // Construir respuesta
        return buildLoginResponse(authUser, accessToken, refreshToken);
    }

    /**
     * Busca un usuario por email en estudiantes y profesores
     */
    private AuthUser findUserByEmail(String email) {
        // Primero buscar en estudiantes
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            return convertStudentToAuthUser(studentOpt.get());
        }

        // Si no es estudiante, buscar en profesores
        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(email);
        if (teacherOpt.isPresent()) {
            return convertTeacherToAuthUser(teacherOpt.get());
        }

        // Si no se encuentra en ninguna tabla
        log.warn("Usuario no encontrado con email: {}", email);
        throw new InvalidCredentialsException("Email o contraseña incorrectos");
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
                .isActive(true) // Los profesores no tienen campo isActive
                .build();
    }

    /**
     * Construye la respuesta del login
     */
    private LoginResponseDto buildLoginResponse(AuthUser authUser, String accessToken, String refreshToken) {
        LoginResponseDto.UserInfoDto userInfo = LoginResponseDto.UserInfoDto.builder()
                .id(authUser.getId())
                .email(authUser.getEmail())
                .name(authUser.getName())
                .userType(authUser.getUserType())
                .roles(authUser.getRoles().stream()
                        .map(UserRole::name)
                        .collect(Collectors.toSet()))
                .loginAt(LocalDateTime.now())
                .build();

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000) // Convertir a segundos
                .user(userInfo)
                .build();
    }
}