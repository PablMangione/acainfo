package com.acainfo.backend.auth.application.imp;

import com.acainfo.backend.auth.application.RegisterTeacherUseCase;
import com.acainfo.backend.auth.domain.exception.AuthenticationException;
import com.acainfo.backend.auth.domain.service.PasswordEncoderService;
import com.acainfo.backend.teacher.application.mapper.TeacherMapper;
import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.domain.exception.DuplicateTeacherException;
import com.acainfo.backend.teacher.domain.repository.CreateTeacherRepository;
import com.acainfo.backend.teacher.domain.repository.ReadTeacherRepository;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherInputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherOutputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementación del caso de uso de registro de profesores.
 * Solo puede ser ejecutado por un administrador.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegisterTeacherUseCaseImp implements RegisterTeacherUseCase {

    private final CreateTeacherRepository createTeacherRepository;
    private final ReadTeacherRepository readTeacherRepository;
    private final PasswordEncoderService passwordEncoder;
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherOutputDto register(TeacherInputDto registerRequest) {
        log.info("Iniciando registro de profesor con email: {}", registerRequest.getEmail());

        // Verificar que el usuario actual es administrador
        validateAdminPermissions();

        // Validar que no exista un profesor con ese email
        if (readTeacherRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Intento de registro con email duplicado: {}", registerRequest.getEmail());
            throw new DuplicateTeacherException("Ya existe un profesor con ese email");
        }

        // Crear el profesor
        Teacher newTeacher = createTeacher(registerRequest);

        // Guardar en la base de datos
        Teacher savedTeacher = createTeacherRepository.save(newTeacher);
        log.info("Profesor registrado exitosamente con ID: {}", savedTeacher.getId());

        // Convertir a DTO y retornar
        return teacherMapper.toOutputDto(savedTeacher);
    }

    /**
     * Valida que el usuario actual tenga permisos de administrador
     */
    private void validateAdminPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Usuario no autenticado");
        }

        boolean isAdmin = authentication.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        if (!isAdmin) {
            log.warn("Usuario sin permisos intentó registrar un profesor: {}",
                    authentication.getName());
            throw new AuthenticationException("No tienes permisos para registrar profesores");
        }

        log.debug("Registro de profesor autorizado por admin: {}", authentication.getName());
    }

    /**
     * Crea una entidad Teacher desde el DTO de registro
     */
    private Teacher createTeacher(TeacherInputDto dto) {
        Teacher teacher = new Teacher();
        teacher.setName(dto.getName());
        teacher.setEmail(dto.getEmail());
        teacher.setPassword(passwordEncoder.encode(dto.getPassword()));
        teacher.setPhoneNumber(dto.getPhoneNumber());
        teacher.setAdmin(dto.getIsAdmin() != null ? dto.getIsAdmin() : false);
        teacher.setRegisteredAt(LocalDateTime.now());

        return teacher;
    }
}