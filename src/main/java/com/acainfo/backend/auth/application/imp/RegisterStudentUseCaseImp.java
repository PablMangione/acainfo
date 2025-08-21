package com.acainfo.backend.auth.application.imp;

import com.acainfo.backend.auth.application.RegisterStudentUseCase;
import com.acainfo.backend.auth.domain.exception.AuthenticationException;
import com.acainfo.backend.auth.domain.model.AuthUser;
import com.acainfo.backend.auth.domain.model.UserRole;
import com.acainfo.backend.auth.domain.model.UserType;
import com.acainfo.backend.auth.domain.service.PasswordEncoderService;
import com.acainfo.backend.auth.domain.service.TokenService;
import com.acainfo.backend.auth.infrastructure.controller.dto.LoginResponseDto;
import com.acainfo.backend.auth.infrastructure.controller.dto.StudentRegisterRequestDto;
import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.domain.exception.DuplicateStudentException;
import com.acainfo.backend.student.domain.repository.CreateStudentRepository;
import com.acainfo.backend.student.domain.repository.ReadStudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso de registro de estudiantes.
 * Crea el estudiante y automáticamente lo autentica.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegisterStudentUseCaseImp implements RegisterStudentUseCase {

    private final CreateStudentRepository createStudentRepository;
    private final ReadStudentRepository readStudentRepository;
    private final PasswordEncoderService passwordEncoder;
    private final TokenService tokenService;

    @Value("${jwt.access-token.expiration:900000}")
    private long accessTokenExpiration;

    @Override
    public LoginResponseDto register(StudentRegisterRequestDto registerRequest) {
        log.info("Iniciando registro de estudiante con email: {}", registerRequest.getEmail());

        // Validar que las contraseñas coincidan
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new AuthenticationException("Las contraseñas no coinciden");
        }

        // Validar que no exista un usuario con ese email
        if (readStudentRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Intento de registro con email duplicado: {}", registerRequest.getEmail());
            throw new DuplicateStudentException("Ya existe un usuario con ese email");
        }

        // Validar términos y condiciones
        if (registerRequest.getAcceptTerms() == null || !registerRequest.getAcceptTerms()) {
            throw new AuthenticationException("Debes aceptar los términos y condiciones");
        }

        // Crear el estudiante
        Student newStudent = createStudent(registerRequest);

        // Guardar en la base de datos
        Student savedStudent = createStudentRepository.save(newStudent);
        log.info("Estudiante registrado exitosamente con ID: {}", savedStudent.getId());

        // Crear AuthUser para generar tokens
        AuthUser authUser = convertToAuthUser(savedStudent);

        // Generar tokens
        String accessToken = tokenService.generateAccessToken(authUser);
        String refreshToken = tokenService.generateRefreshToken(authUser);

        // Construir y retornar respuesta (automáticamente logueado)
        return buildLoginResponse(authUser, accessToken, refreshToken);
    }

    /**
     * Crea una entidad Student desde el DTO de registro
     */
    private Student createStudent(StudentRegisterRequestDto dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setMajor(dto.getMajor());
        student.setActive(true);
        student.setRegisteredAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        return student;
    }

    /**
     * Convierte un Student a AuthUser
     */
    private AuthUser convertToAuthUser(Student student) {
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
     * Construye la respuesta del login tras el registro
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