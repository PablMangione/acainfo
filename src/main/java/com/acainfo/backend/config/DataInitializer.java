package com.acainfo.backend.config;

import com.acainfo.backend.auth.domain.service.PasswordEncoderService;
import com.acainfo.backend.config.properties.AppProperties;
import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.domain.repository.CreateStudentRepository;
import com.acainfo.backend.student.domain.repository.ReadStudentRepository;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.repository.CreateSubjectRepository;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.domain.repository.CreateTeacherRepository;
import com.acainfo.backend.teacher.domain.repository.ReadTeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Inicializador de datos de prueba para desarrollo.
 * Solo se ejecuta en el perfil 'dev' y si está habilitado.
 */
@Configuration
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final PasswordEncoderService passwordEncoder;
    private final CreateTeacherRepository createTeacherRepository;
    private final ReadTeacherRepository readTeacherRepository;
    private final CreateStudentRepository createStudentRepository;
    private final ReadStudentRepository readStudentRepository;
    private final CreateSubjectRepository createSubjectRepository;
    private final ReadSubjectRepository readSubjectRepository;
    private final AppProperties appProperties;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (!appProperties.getInitData().getEnabled()) {
                log.info("Inicialización de datos deshabilitada");
                return;
            }

            log.info("====================================");
            log.info("Iniciando carga de datos de prueba");
            log.info("====================================");

            createTeachers();
            createStudents();
            createSubjects();

            log.info("====================================");
            log.info("Datos de prueba cargados exitosamente");
            log.info("====================================");

            printCredentials();
        };
    }

    private void createTeachers() {
        log.info("Creando profesores de prueba...");

        String adminEmail = appProperties.getInitData().getAdminEmail();
        String adminPassword = appProperties.getInitData().getAdminPassword();
        String adminName = appProperties.getInitData().getAdminName();

        // Admin principal
        if (appProperties.getInitData().getCreateAdmin() && !readTeacherRepository.existsByEmail(adminEmail)) {
            Teacher admin = new Teacher();
            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setPhoneNumber("+34600000001");
            admin.setAdmin(true);
            admin.setRegisteredAt(LocalDateTime.now());
            createTeacherRepository.save(admin);
            log.info("✓ Admin creado: {}", adminEmail);
        }

        // Profesores regulares
        List<TeacherData> teachers = List.of(
                new TeacherData("Juan Pérez García", "juan.perez@acainfo.dev", "Teacher123", "+34600000002", false),
                new TeacherData("María López Fernández", "maria.lopez@acainfo.dev", "Teacher123", "+34600000003", false),
                new TeacherData("Carlos Martín Ruiz", "carlos.martin@acainfo.dev", "Teacher123", "+34600000004", true),
                new TeacherData("Ana Sánchez Torres", "ana.sanchez@acainfo.dev", "Teacher123", "+34600000005", false)
        );

        for (TeacherData data : teachers) {
            if (!readTeacherRepository.existsByEmail(data.email)) {
                Teacher teacher = new Teacher();
                teacher.setName(data.name);
                teacher.setEmail(data.email);
                teacher.setPassword(passwordEncoder.encode(data.password));
                teacher.setPhoneNumber(data.phone);
                teacher.setAdmin(data.isAdmin);
                teacher.setRegisteredAt(LocalDateTime.now());
                createTeacherRepository.save(teacher);
                log.info("✓ Profesor creado: {}", data.email);
            }
        }
    }

    private void createStudents() {
        log.info("Creando estudiantes de prueba...");

        List<StudentData> students = List.of(
                new StudentData("Laura", "González Díaz", "laura.gonzalez@student.dev", "Student123", "+34610000001", Major.ING_INF),
                new StudentData("Pablo", "Rodríguez López", "pablo.rodriguez@student.dev", "Student123", "+34610000002", Major.ING_INF),
                new StudentData("Sofía", "Martínez García", "sofia.martinez@student.dev", "Student123", "+34610000003", Major.ING_IND),
                new StudentData("Diego", "Fernández Ruiz", "diego.fernandez@student.dev", "Student123", "+34610000004", Major.ING_IND),
                new StudentData("Carmen", "López Sánchez", "carmen.lopez@student.dev", "Student123", "+34610000005", Major.ING_INF),
                new StudentData("Alejandro", "García Pérez", "alejandro.garcia@student.dev", "Student123", "+34610000006", Major.ING_IND)
        );

        for (StudentData data : students) {
            if (!readStudentRepository.existsByEmail(data.email)) {
                Student student = new Student();
                student.setName(data.name);
                student.setLastName(data.lastName);
                student.setEmail(data.email);
                student.setPassword(passwordEncoder.encode(data.password));
                student.setPhoneNumber(data.phone);
                student.setMajor(data.major);
                student.setActive(true);
                student.setRegisteredAt(LocalDateTime.now());
                student.setUpdatedAt(LocalDateTime.now());
                createStudentRepository.save(student);
                log.info("✓ Estudiante creado: {}", data.email);
            }
        }
    }

    private void createSubjects() {
        log.info("Creando asignaturas de prueba...");

        List<SubjectData> subjects = new ArrayList<>();

        // Ingeniería Informática
        subjects.addAll(List.of(
                // Primer año
                new SubjectData("Álgebra Lineal", Major.ING_INF, CourseYear.FIRST, Quarter.FIRST),
                new SubjectData("Cálculo I", Major.ING_INF, CourseYear.FIRST, Quarter.FIRST),
                new SubjectData("Fundamentos de Programación", Major.ING_INF, CourseYear.FIRST, Quarter.FIRST),
                new SubjectData("Física I", Major.ING_INF, CourseYear.FIRST, Quarter.FIRST),
                new SubjectData("Cálculo II", Major.ING_INF, CourseYear.FIRST, Quarter.SECOND),
                new SubjectData("Programación Orientada a Objetos", Major.ING_INF, CourseYear.FIRST, Quarter.SECOND),
                new SubjectData("Física II", Major.ING_INF, CourseYear.FIRST, Quarter.SECOND),
                new SubjectData("Matemática Discreta", Major.ING_INF, CourseYear.FIRST, Quarter.SECOND),

                // Segundo año
                new SubjectData("Estructuras de Datos", Major.ING_INF, CourseYear.SECOND, Quarter.FIRST),
                new SubjectData("Bases de Datos", Major.ING_INF, CourseYear.SECOND, Quarter.FIRST),
                new SubjectData("Arquitectura de Computadores", Major.ING_INF, CourseYear.SECOND, Quarter.FIRST),
                new SubjectData("Estadística", Major.ING_INF, CourseYear.SECOND, Quarter.FIRST),
                new SubjectData("Algoritmos y Complejidad", Major.ING_INF, CourseYear.SECOND, Quarter.SECOND),
                new SubjectData("Sistemas Operativos", Major.ING_INF, CourseYear.SECOND, Quarter.SECOND),
                new SubjectData("Redes de Computadores", Major.ING_INF, CourseYear.SECOND, Quarter.SECOND),
                new SubjectData("Ingeniería del Software I", Major.ING_INF, CourseYear.SECOND, Quarter.SECOND)
        ));

        // Ingeniería Industrial
        subjects.addAll(List.of(
                // Primer año
                new SubjectData("Álgebra Lineal", Major.ING_IND, CourseYear.FIRST, Quarter.FIRST),
                new SubjectData("Cálculo I", Major.ING_IND, CourseYear.FIRST, Quarter.FIRST),
                new SubjectData("Física I", Major.ING_IND, CourseYear.FIRST, Quarter.FIRST),
                new SubjectData("Química General", Major.ING_IND, CourseYear.FIRST, Quarter.FIRST),
                new SubjectData("Cálculo II", Major.ING_IND, CourseYear.FIRST, Quarter.SECOND),
                new SubjectData("Física II", Major.ING_IND, CourseYear.FIRST, Quarter.SECOND),
                new SubjectData("Expresión Gráfica", Major.ING_IND, CourseYear.FIRST, Quarter.SECOND),
                new SubjectData("Fundamentos de Informática", Major.ING_IND, CourseYear.FIRST, Quarter.SECOND)
        ));

        for (SubjectData data : subjects) {
            // Verificar si ya existe
            List<Subject> existing = readSubjectRepository.findByMajorAndCourseYearAndQuarter(
                    data.major, data.courseYear, data.quarter
            );

            boolean exists = existing.stream()
                    .anyMatch(s -> s.getName().equals(data.name));

            if (!exists) {
                Subject subject = new Subject();
                subject.setName(data.name);
                subject.setMajor(data.major);
                subject.setCourseYear(data.courseYear);
                subject.setQuarter(data.quarter);
                subject.setActive(true);
                subject.setCreatedAt(LocalDateTime.now());
                subject.setUpdatedAt(LocalDateTime.now());
                createSubjectRepository.save(subject);
                log.info("✓ Asignatura creada: {} - {} {} {}",
                        data.name, data.major, data.courseYear, data.quarter);
            }
        }
    }

    private void printCredentials() {
        String adminEmail = appProperties.getInitData().getAdminEmail();
        String adminPassword = appProperties.getInitData().getAdminPassword();

        log.info("====================================");
        log.info("CREDENCIALES DE PRUEBA");
        log.info("====================================");
        log.info("Admin:");
        log.info("  Email: {}", adminEmail);
        log.info("  Password: {}", adminPassword);
        log.info("------------------------------------");
        log.info("Profesor (con admin):");
        log.info("  Email: carlos.martin@acainfo.dev");
        log.info("  Password: Teacher123");
        log.info("------------------------------------");
        log.info("Profesor (sin admin):");
        log.info("  Email: juan.perez@acainfo.dev");
        log.info("  Password: Teacher123");
        log.info("------------------------------------");
        log.info("Estudiante:");
        log.info("  Email: laura.gonzalez@student.dev");
        log.info("  Password: Student123");
        log.info("====================================");
    }

    // Clases auxiliares para datos
    private record TeacherData(String name, String email, String password, String phone, boolean isAdmin) {}
    private record StudentData(String name, String lastName, String email, String password, String phone, Major major) {}
    private record SubjectData(String name, Major major, CourseYear courseYear, Quarter quarter) {}
}