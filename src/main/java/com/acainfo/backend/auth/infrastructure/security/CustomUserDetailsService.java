package com.acainfo.backend.auth.infrastructure.security;

import com.acainfo.backend.auth.domain.model.AuthUser;
import com.acainfo.backend.auth.domain.model.UserRole;
import com.acainfo.backend.auth.domain.model.UserType;
import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.domain.repository.ReadStudentRepository;
import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.domain.repository.ReadTeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio personalizado para cargar usuarios desde la base de datos.
 * Unifica la búsqueda de estudiantes y profesores.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final ReadStudentRepository studentRepository;
    private final ReadTeacherRepository teacherRepository;

    /**
     * Carga un usuario por su username (que en nuestro caso es el ID compuesto)
     * Formato: "STUDENT_123" o "TEACHER_456"
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Cargando usuario: {}", username);

        // Parsear el ID compuesto
        String[] parts = username.split("_");
        if (parts.length != 2) {
            // Si no tiene el formato esperado, intentar buscar por email
            return loadUserByEmail(username);
        }

        String type = parts[0];
        Long id = Long.parseLong(parts[1]);

        AuthUser authUser = switch (type) {
            case "STUDENT" -> loadStudent(id);
            case "TEACHER" -> loadTeacher(id);
            default -> throw new UsernameNotFoundException("Tipo de usuario desconocido: " + type);
        };

        return createUserDetails(authUser);
    }

    /**
     * Intenta cargar un usuario por email (usado en el login)
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        // Primero buscar en estudiantes
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            AuthUser authUser = convertStudentToAuthUser(studentOpt.get());
            return createUserDetails(authUser);
        }

        // Si no es estudiante, buscar en profesores
        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(email);
        if (teacherOpt.isPresent()) {
            AuthUser authUser = convertTeacherToAuthUser(teacherOpt.get());
            return createUserDetails(authUser);
        }

        throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
    }

    /**
     * Carga un estudiante por ID
     */
    private AuthUser loadStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Estudiante no encontrado: " + id));

        return convertStudentToAuthUser(student);
    }

    /**
     * Carga un profesor por ID
     */
    private AuthUser loadTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Profesor no encontrado: " + id));

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
                .isActive(true) // Los profesores no tienen campo isActive
                .build();
    }

    /**
     * Crea un UserDetails de Spring Security a partir de un AuthUser
     */
    private UserDetails createUserDetails(AuthUser authUser) {
        if (!authUser.isActive()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + authUser.getEmail());
        }

        Set<SimpleGrantedAuthority> authorities = authUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());

        return User.builder()
                .username(authUser.getId())
                .password(authUser.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!authUser.isActive())
                .build();
    }
}