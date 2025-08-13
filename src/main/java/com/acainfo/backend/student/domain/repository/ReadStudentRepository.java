package com.acainfo.backend.student.domain.repository;

import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.globalenum.Major;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de lectura para la entidad Student.
 *
 * Contiene operaciones de consulta sin efectos secundarios.
 * Los filtrados complejos y validaciones se manejan en la capa de servicio.
 */
public interface ReadStudentRepository {

    /**
     * Busca un estudiante por su ID.
     */
    Optional<Student> findById(Long id);

    /**
     * Obtiene todos los estudiantes.
     */
    List<Student> findAll();

    /**
     * Verifica si existe un estudiante con el ID dado.
     */
    boolean existsById(Long id);

    /**
     * Busca un estudiante por su email.
     * Útil para autenticación y validación de unicidad.
     */
    Optional<Student> findByEmail(String email);

    /**
     * Verifica si existe un estudiante con el email dado.
     */
    boolean existsByEmail(String email);

    /**
     * Busca estudiantes por carrera.
     */
    List<Student> findByMajor(Major major);

    /**
     * Busca estudiantes activos.
     */
    List<Student> findByIsActiveTrue();

    /**
     * Busca estudiantes activos por carrera.
     */
    List<Student> findByMajorAndIsActiveTrue(Major major);

    /**
     * Búsqueda por texto en el nombre (case-insensitive).
     */
    List<Student> findByNameContainingIgnoreCase(String name);

    /**
     * Búsqueda por texto en el apellido (case-insensitive).
     */
    List<Student> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Búsqueda por nombre y apellido (case-insensitive).
     */
    List<Student> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String name, String lastName);
}